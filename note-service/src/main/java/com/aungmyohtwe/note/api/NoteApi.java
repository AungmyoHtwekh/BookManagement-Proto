package com.aungmyohtwe.note.api;

import com.aungmyohtwe.note.constant.Constant;
import com.aungmyohtwe.note.model.Note;
import com.aungmyohtwe.note.payload.ResponseStatus;
import com.aungmyohtwe.note.service.NoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class NoteApi {

    private final static Logger log = Logger.getLogger(NoteApi.class);
    @Autowired
    NoteService noteService;
    @RequestMapping(value = "/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> saveNote(@RequestBody Note note, HttpServletRequest request, HttpServletResponse response){

        try {
            noteService.saveNote(initObject(note));
            ResponseStatus responseStatus;
            if (note.getId() != null){
                responseStatus = setResponse(Constant.SUCCESS,Constant.UPDATED,Constant.SUCCESS_CODE);
            }else {
                responseStatus = setResponse(Constant.SUCCESS,Constant.DESCRIPTION_SUCCESS,Constant.SUCCESS_CODE);
            }
            return new ResponseEntity<>(responseStatus, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> deleteNote(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){

        try {
            Note note = noteService.findById(id);
            if (note != null){
                noteService.deleteNote(note);
                ResponseStatus responseStatus = setResponse(Constant.SUCCESS,Constant.DESCRIPTION_DELETE,Constant.SUCCESS_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.OK);
            }else {
                ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.NOT_FOUND,Constant.NOT_FOUND_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/search",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getNotes(@RequestParam("keyword") String keyword, HttpServletRequest request, HttpServletResponse response){

        try {
            List<Note> noteList = noteService.findByKeyword(keyword);
            if (noteList != null && noteList.size() > 0){
                log.info("Getting Notes : " + noteList.size());
                return new ResponseEntity<>(noteList, HttpStatus.OK);
            }else {
                ResponseStatus responseStatus = setResponse(Constant.NO_RECORD_FOUND,Constant.NO_RECORD_FOUND,Constant.NOT_FOUND_CODE);
                return new ResponseEntity<>(responseStatus, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.error("Exception happen in : " + e.getMessage());
            ResponseStatus responseStatus = setResponse(Constant.FAIL,Constant.SYSTEM_ERROR,Constant.ERROR_CODE);
            return new ResponseEntity<>(responseStatus, HttpStatus.BAD_REQUEST);
        }

    }

    private Note initObject(Note note){
        Note noteObj = new Note();
        if (note.getId() != null){
            noteObj.setId(note.getId());
        }
        noteObj.setCreatedDate(new Date());
        noteObj.setTitle(note.getTitle());
        noteObj.setMessage(note.getMessage());
        return noteObj;
    }

    private ResponseStatus setResponse(String status, String description, String code){
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setStatus(status);
        responseStatus.setDescription(description);
        responseStatus.setCode(code);
        return responseStatus;
    }
}
