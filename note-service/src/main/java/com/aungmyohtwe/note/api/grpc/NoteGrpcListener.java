package com.aungmyohtwe.note.api.grpc;

import com.aungmyohtwe.note.model.*;
import com.aungmyohtwe.note.proto.model.NoteServiceGrpc;
import com.aungmyohtwe.note.proto.model.ParamId;
import com.aungmyohtwe.note.proto.model.Response;
import com.aungmyohtwe.note.proto.model.ResponseList;
import com.aungmyohtwe.note.service.NoteService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@GRpcService
public class NoteGrpcListener extends NoteServiceGrpc.NoteServiceImplBase {

    @Autowired
    NoteService noteService;

    @Override
    public void createNote(com.aungmyohtwe.note.proto.model.Note request, StreamObserver<Response> responseObserver) {

        if (request != null){
            try {
                Note note = new Note();
                note.setTitle(request.getTitle());
                note.setMessage(request.getMessage());
                note.setCreatedDate(new Date());
                noteService.saveNote(note);
                Response.Builder builder = Response.newBuilder();
                builder.setCode("200");
                builder.setStatus("Success");
                builder.setDescription("Successfully saved.");
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
            }catch (Exception e){
                responseObserver.onError(e);
            }

        }else {
            responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteNote(ParamId request, StreamObserver<Response> responseObserver) {
        if (request != null){
            try {
                Note note = noteService.findById(Long.valueOf(request.getId()));
                if (note != null){
                    noteService.deleteNote(note);
                    Response.Builder builder = Response.newBuilder();
                    builder.setCode("200");
                    builder.setStatus("Success");
                    builder.setDescription("Successfully saved.");
                    responseObserver.onNext(builder.build());
                    responseObserver.onCompleted();
                }
            }catch (Exception e){
                responseObserver.onError(e);
            }
        }else {
            responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void searchNotes(ParamId request, StreamObserver<ResponseList> responseObserver) {
        if (request != null){
            try {
                List<Note> noteList = noteService.findByKeyword(request.getId());
                ResponseList.Builder responseBuilder = ResponseList.newBuilder();
                if (noteList != null && noteList.size() > 0){
                    for (Note note : noteList){
                        com.aungmyohtwe.note.proto.model.Note.Builder noteBuilder = com.aungmyohtwe.note.proto.model.Note.newBuilder();
                        noteBuilder.setId(String.valueOf(note.getId()));
                        noteBuilder.setTitle(note.getTitle());
                        noteBuilder.setMessage(note.getMessage());
                        noteBuilder.setCreatedDate(note.getCreatedDate().toString());
                        responseBuilder.addNotes(noteBuilder);
                    }
                }
                responseObserver.onNext(responseBuilder.build());
                responseObserver.onCompleted();
            }catch (Exception e){
                responseObserver.onError(e);
            }
        }else {
            responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT));
            responseObserver.onCompleted();
        }
    }
}
