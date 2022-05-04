package com.aungmyohtwe.note;

import com.aungmyohtwe.note.api.NoteApi;
import com.aungmyohtwe.note.model.Note;
import com.aungmyohtwe.note.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteApiTest {

    @InjectMocks
    NoteApi noteApi;

    @Autowired
    MockMvc mockMvc;

    @Mock
    NoteService noteService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc =  MockMvcBuilders.standaloneSetup(noteApi).build();
    }

    @Test
    public void saveNoteTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        Note note = new Note();
        note.setTitle("Remider");
        note.setMessage("To go shopping this weekend.");
        String requestJson = ow.writeValueAsString(note );
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/save")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteNoteTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchNoteTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/search?keyword=to")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
