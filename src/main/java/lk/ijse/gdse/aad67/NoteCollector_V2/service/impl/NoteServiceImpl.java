package lk.ijse.gdse.aad67.NoteCollector_V2.service.impl;

import lk.ijse.gdse.aad67.NoteCollector_V2.customStatusCodes.SelectedUserAndNoteErrorStatus;
import lk.ijse.gdse.aad67.NoteCollector_V2.dao.NoteDao;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.NoteStatus;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.impl.NoteDTO;
import lk.ijse.gdse.aad67.NoteCollector_V2.entity.impl.NoteEntity;
import lk.ijse.gdse.aad67.NoteCollector_V2.exception.DataPersistException;
import lk.ijse.gdse.aad67.NoteCollector_V2.exception.NoteNotFoundException;
import lk.ijse.gdse.aad67.NoteCollector_V2.service.NoteService;
import lk.ijse.gdse.aad67.NoteCollector_V2.util.AppUtil;
import lk.ijse.gdse.aad67.NoteCollector_V2.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteDao noteDao;
    @Autowired
    private Mapping mapping;
    @Override
    public void saveNote(NoteDTO noteDTO) {
        noteDTO.setNoteId(AppUtil.generateNoteId());
        NoteEntity saveNote = noteDao.save(mapping.toNoteEntity(noteDTO));
        if(saveNote == null){
            throw new DataPersistException("Note not saved");
        }
    }

    @Override
    public List<NoteDTO> getAllNotes() {
        return mapping.asNoteDTOList( noteDao.findAll());
    }

    @Override
    public NoteStatus getNote(String noteId) {
        if(noteDao.existsById(noteId)){
            var selectedUser = noteDao.getReferenceById(noteId);
            return mapping.toNoteDTO(selectedUser);
        }else {
            return new SelectedUserAndNoteErrorStatus(2,"Selected note not found");
        }
    }

    @Override
    public void deleteNote(String noteId) {
        Optional<NoteEntity> foundNote = noteDao.findById(noteId);
        if (!foundNote.isPresent()) {
            throw new NoteNotFoundException("Note not found");
        }else {
            noteDao.deleteById(noteId);
        }
    }

    @Override
    public void updateNote(String noteId,NoteDTO noteDTO) {
        Optional<NoteEntity> tmpNote = noteDao.findById(noteId);
        if(!tmpNote.isPresent()){
            throw new NoteNotFoundException("Note not found");
        }else{
            tmpNote.get().setNoteTitle(noteDTO.getNoteTitle());
            tmpNote.get().setNoteDesc(noteDTO.getNoteDesc());
            tmpNote.get().setCreatedDate(noteDTO.getCreatedDate());
            tmpNote.get().setPriorityLevel(noteDTO.getPriorityLevel());
        }
    }
}
