package com.likelion.picpic.service;

import com.likelion.picpic.DataNotFoundException;
import com.likelion.picpic.domain.Memo;
import com.likelion.picpic.domain.PhotoBook;
import com.likelion.picpic.domain.User;
import com.likelion.picpic.dto.CreatePhotoBookDto;
import com.likelion.picpic.dto.GetPhotosAndMemosAndUuidDto;
import com.likelion.picpic.dto.GetPhotosAndMemosDto;
import com.likelion.picpic.repository.PhotoBookRepository;
import com.likelion.picpic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoBookService {
    private final PhotoBookRepository photoBookRepository;
    private final UserRepository userRepository;

    public void savePhotoBook(Long userId, CreatePhotoBookDto createPhotoBookDto){  //이미 포토북 있거나, 유저 못찾으면 오류
        Optional<User> optUser=userRepository.findById(userId);
        if(optUser.isPresent()){
            User user=optUser.get();
            if(photoBookRepository.findByUser(user).isPresent())
                throw new DataNotFoundException("포토북이 이미 존재합니다.");
            else photoBookRepository.save(PhotoBook.from(user, createPhotoBookDto));
        }
        else throw new DataNotFoundException("해당 유저를 찾지 못하였습니다.");
    }

    public GetPhotosAndMemosDto getPhotoList(String uuid){
        Optional<User> optUser=userRepository.findByUuid(uuid);
        if(optUser.isEmpty()) throw new DataNotFoundException("유저를 찾지 못하였습니다.");
        User user=optUser.get();
        Optional<PhotoBook> optPhotoBook=photoBookRepository.findByUser(user);
        if(optPhotoBook.isEmpty()) throw new DataNotFoundException("포토북이 존재하지 않습니다.");
        PhotoBook photoBook=optPhotoBook.get();
        List<Memo> memoList=photoBook.getMemoList();
        List<String> photoList=photoBook.getPhotos();
        GetPhotosAndMemosDto dto=new GetPhotosAndMemosDto();
        dto.setPhotoList(photoList);
        dto.setMemoList(memoList);
        return dto;
    }

    public GetPhotosAndMemosAndUuidDto getPhotosAndUuid(String email){
        Optional<User> optUser=userRepository.findByEmail(email);
        if(optUser.isEmpty()) throw new DataNotFoundException("유저를 찾지 못하였습니다.");
        User user=optUser.get();
        Optional<PhotoBook> optPhotoBook=photoBookRepository.findByUser(user);
        if(optPhotoBook.isEmpty()) throw new DataNotFoundException("포토북이 존재하지 않습니다.");
        PhotoBook photoBook=optPhotoBook.get();
        List<Memo> memoList=photoBook.getMemoList();
        List<String> photoList=photoBook.getPhotos();
        GetPhotosAndMemosAndUuidDto dto=new GetPhotosAndMemosAndUuidDto();
        dto.setPhotoList(photoList);
        dto.setMemoList(memoList);
        dto.setUuid(user.getUuid());
        return dto;
    }
}
