package lk.ijse.gdse.aad67.NoteCollector_V2.controller;


import lk.ijse.gdse.aad67.NoteCollector_V2.customStatusCodes.SelectedUserAndNoteErrorStatus;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.NoteStatus;
import lk.ijse.gdse.aad67.NoteCollector_V2.dto.impl.NoteDTO;
import lk.ijse.gdse.aad67.NoteCollector_V2.exception.DataPersistException;
import lk.ijse.gdse.aad67.NoteCollector_V2.exception.NoteNotFoundException;
import lk.ijse.gdse.aad67.NoteCollector_V2.service.NoteService;
import lk.ijse.gdse.aad67.NoteCollector_V2.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveNote(@RequestBody NoteDTO noteDTO){
        try {
            noteService.saveNote(noteDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{noteID}",produces = MediaType.APPLICATION_JSON_VALUE)
    public NoteStatus getSelectedNote(@PathVariable ("noteID") String noteId){
        if (!Regex.noteIdMatcher(noteId)) {
            return new SelectedUserAndNoteErrorStatus(1,"Note id not valid");
        }
        return noteService.getNote(noteId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NoteDTO> getAllNotes(){
        return noteService.getAllNotes();
    }

    @DeleteMapping(value = "/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable ("noteId") String noteId){
        try {
            if (!Regex.noteIdMatcher(noteId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            noteService.deleteNote(noteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoteNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{noteId}")
    public ResponseEntity<Void> updateNote(@PathVariable ("noteId") String noteId,
                           @RequestBody NoteDTO noteDTO){
        try{
            if(!Regex.noteIdMatcher(noteId) || noteDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            noteService.updateNote(noteId,noteDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoteNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
