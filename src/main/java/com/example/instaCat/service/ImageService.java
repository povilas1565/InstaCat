package com.example.instaCat.service;

import com.example.instaCat.entity.Image;
import com.example.instaCat.entity.Post;
import com.example.instaCat.entity.User;
import com.example.instaCat.exceptions.ImageNotFoundException;
import com.example.instaCat.repository.ImageRepository;
import com.example.instaCat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/*В этом сервисе мы будем загружать изображения для профиля и для поста*/
@Service
public class ImageService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Image uploadImageToProfile(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);

        //проверяем - есть ли уже фотография пользователя в базе.
        //если есть - тогда удаляем и загружаем новую
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(compressImage(file.getBytes()));
        image.setName(file.getName());
        LOG.info("Upload image to user {}", user.getId());

        return imageRepository.save(image);
    }

    public Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(singlePostCollector()); //так мы добиваемся 100% уникальности поста

        Image image = new Image();
        image.setPostId(post.getId());
        image.setImageBytes(compressImage(file.getBytes()));
        image.setName(file.getName());
        LOG.info("Upload image to post {}", post.getId());

        return imageRepository.save(image);
    }

    public Image getUserProfileImage(Principal principal) {
        User user = getUserByPrincipal(principal);

        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            userProfileImage.setImageBytes(decompressImage(userProfileImage.getImageBytes()));
        }
        return userProfileImage;
    }

    public Image getPostImage(Long postId) {
        Image postImage = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Image cannot found for post " + postId));
        if (!ObjectUtils.isEmpty(postImage)) {
            postImage.setImageBytes(decompressImage(postImage.getImageBytes()));
        }
        return postImage;
    }

    //компрессия фото
    //ссылка на похожую реализацию:  https://russianblogs.com/article/6125740221/
    //еще одна: https://russianblogs.com/article/13701199711/
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] segment = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(segment); //фактическое количество байтов сжатых данных, записанных в выходной буфер
            byteArrayOutputStream.write(segment, 0, count);
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress image");
        }

        System.out.println("Compressed image size = " + byteArrayOutputStream.toByteArray().length);
        return byteArrayOutputStream.toByteArray();
    }

    //декомпрессия фото
    private static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] segment = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(segment);
                byteArrayOutputStream.write(segment, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress image");
        }
        return byteArrayOutputStream.toByteArray();
    }


    //у пользователя мб очень много постов, но естественно нам требуется только один - тот к которому мы привязываем изображение
    //а вдруг случится так, что окажется 2 поста с одинаковым id
    //напишем метод generic для получения единственного поста
    //https://metanit.com/java/tutorial/10.6.php
    public <T> Collector<T, ?, T> singlePostCollector() {
        // Метод collectingAndThen (нисходящий сборщик, завершитель функций) сборщиков классов в Java, который принимает Collector, чтобы мы могли выполнить дополнительное завершающее преобразование.
        return Collectors.collectingAndThen(
                Collectors.toList(), // Коллектор, который собирает все входные элементы в список в порядке встречи
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}