package lk.ijse.gdse.aad67.NoteCollector_V2.util;

import lk.ijse.gdse.aad67.NoteCollector_V2.dto.impl.NoteDTO;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.impl.UserDTO;
import lk.ijse.gdse.aad67.NoteCollector_V2.entity.impl.NoteEntity;
import lk.ijse.gdse.aad67.NoteCollector_V2.entity.impl.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;

    //for user mapping
    public UserEntity toUserEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, UserEntity.class);
    }
    public UserDTO toUserDTO(UserEntity userEntity){
        return modelMapper.map(userEntity, UserDTO.class);
    }

    public List<UserDTO> asUserDTOList(List<UserEntity> userEntityList){
        return modelMapper.map(userEntityList, new TypeToken<List<UserDTO>>() {}.getType());
    }

    //for note mapping
    public NoteDTO toNoteDTO(NoteEntity noteEntity) {
        return modelMapper.map(noteEntity, NoteDTO.class);
    }
    public NoteEntity toNoteEntity(NoteDTO noteDTO) {
        return modelMapper.map(noteDTO, NoteEntity.class);
    }
    public List<NoteDTO> asNoteDTOList(List<NoteEntity> noteEntities) {
        return modelMapper.map(noteEntities, new TypeToken<List<NoteDTO>>() {}.getType());
    }

}
