package com.example.hogwarts.service;

import com.example.hogwarts.models.Avatar;
import com.example.hogwarts.models.Faculty;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(long studentId);

//   List<Avatar> findAllByPage ();

//    List<Avatar> findAllByPage(Integer page, Integer size);

}
