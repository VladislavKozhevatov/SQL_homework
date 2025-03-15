package com.example.hogwarts.service;


import com.example.hogwarts.models.Avatar;
import com.example.hogwarts.models.Student;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.swagger.v3.core.util.AnnotationsUtils.getExtensions;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService{
private AvatarRepository avatarRepository;
private StudentRepository studentRepository;


@Value("${path.to.avatars.folder}")
private String avatarsDir;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getById(studentId); // ищем студента по Id
        Path filePath = Path.of(avatarsDir,student + "." + getExtensions(avatarFile.getOriginalFilename()));//Path - Это интерфейс. В нем будем хранить путь до директории с загружаемыми файлами.
        Files.createDirectories(filePath.getParent());            //Создаем нужную нам директорию для хранения данных
        Files.deleteIfExists(filePath);                           //удаляем из нее файл, если он уже присутствует там.
        try(                                                      //конструкция нам нужна, чтобы следить за закрытием открытых ресурсов.
            InputStream is = avatarFile.getInputStream();         //Открываем входной поток данной командой
            OutputStream os = Files.newOutputStream(filePath,CREATE_NEW); //создадим выходной поток
            BufferedInputStream bis = new BufferedInputStream(is, 1024); //буферизированные потоки чтобы передавать не по одному байту из входного потока в выходной, а пачками байтов
            BufferedOutputStream bos = new BufferedOutputStream(os,1024);
        ){
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId); // поиск аватара по id студента
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString()); //указываем путь к файлу
        avatar.setFileSize(avatarFile.getSize());//указываем размер файла
        avatar.setMediaType(avatarFile.getContentType());//указываем тип файла
        avatar.setData(generateDataForDB(filePath)); // генерим данные для БД чтобы сохранить аватар туда
        avatarRepository.save(avatar); //сохраняем
    }

    private byte[] generateDataForDB (Path filePath) throws IOException{
        try(
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is,1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            BufferedImage image = ImageIO.read(bis);

            //пишем код для создания preview аватарки в сжатом виде
            int height = image.getHeight()/(image.getWidth()/100);
            BufferedImage preview = new BufferedImage(100,height,image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image,0,0,100,height,null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()),baos);
            return baos.toByteArray(); //ByteArrayOutputStream- преобразовываем в выходящий поток байтов

        }
    }

    @Override
    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar()); //метод ищет аватарку по id cтудента и в случае если не находит , то создаёт
    }



//    @Override
//    public List<Avatar> findAllByPage(Integer page, Integer size) {
//        PageRequest pageRequest = PageRequest.of(page - 1, size );
//        return avatarRepository.findAll(pageRequest).getContent();
//    }



    private String getExtensions(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
}
