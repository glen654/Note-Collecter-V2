package lk.ijse.gdse.aad67.NoteCollector_V2.controller;


import lk.ijse.gdse.aad67.NoteCollector_V2.customStatusCodes.SelectedUserAndNoteErrorStatus;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.UserStatus;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.impl.UserDTO;
import lk.ijse.gdse.aad67.NoteCollector_V2.exception.DataPersistException;
import lk.ijse.gdse.aad67.NoteCollector_V2.exception.UserNotFoundException;
import lk.ijse.gdse.aad67.NoteCollector_V2.service.UserService;
import lk.ijse.gdse.aad67.NoteCollector_V2.util.AppUtil;
import lk.ijse.gdse.aad67.NoteCollector_V2.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUser(@RequestPart ("firstName") String firstName,
                                   @RequestPart ("lastName") String lastName,
                                   @RequestPart ("email") String email,
                                   @RequestPart ("password") String password,
                                   @RequestPart ("profilePic") MultipartFile profilePic

    ) {

        System.out.println("RAW pro pic " + profilePic);
        // profilePic ----> Base64
        String base64ProPic = "";

        try {
            byte[] bytesProPic = profilePic.getBytes();
            base64ProPic = AppUtil.profilePicToBase64(bytesProPic);
            //User ID Generate
            String userId = AppUtil.generateUserId();

            //TODO:Build The Object
            UserDTO buildUserDto = new UserDTO();
            buildUserDto.setUserId(userId);
            buildUserDto.setFirstName(firstName);
            buildUserDto.setLastName(lastName);
            buildUserDto.setEmail(email);
            buildUserDto.setPassword(password);
            buildUserDto.setProfilePic(base64ProPic);
            userService.saveUser(buildUserDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{userId}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public UserStatus getSelectedUser(@PathVariable ("userId") String userId){
        if(!Regex.userIdMatcher(userId)){
           return new SelectedUserAndNoteErrorStatus(1,"User ID is not valid");
        }
        return userService.getUser(userId);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable ("userId") String userId){
        try {
            if(!Regex.userIdMatcher(userId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateUser(@RequestPart ("firstName") String firstName,
                           @RequestPart ("lastName") String lastName,
                           @RequestPart ("email") String email,
                           @RequestPart ("password") String password,
                           @RequestPart ("profilePic") MultipartFile profilePic,
                           @PathVariable ("userId") String userId
    ){
        System.out.println("RAW pro pic "+profilePic);
        String base64ProPic = "";

        try {
            byte [] bytesProPic = profilePic.getBytes();
            base64ProPic = AppUtil.profilePicToBase64(bytesProPic);

        }catch (Exception e){
            e.printStackTrace();
        }

        //TODO:Build The Object
        UserDTO buildUserDto = new UserDTO();
        buildUserDto.setUserId(userId);
        buildUserDto.setFirstName(firstName);
        buildUserDto.setLastName(lastName);
        buildUserDto.setEmail(email);
        buildUserDto.setPassword(password);
        buildUserDto.setProfilePic(base64ProPic);
        userService.saveUser(buildUserDto);
        userService.updateUser(userId,buildUserDto);
    }
}
