package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.ActorConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActorService {

    private ActorConverter converter;
    private ActorDao dao;

    @Autowired
    public ActorService(ActorConverter converter, ActorDao dao) {
        this.converter = converter;
        this.dao = dao;
    }

    public ActorDto addActor(ActorDto dto) {
        ActorEntity entityActor = converter.convert(dto);
        entityActor.setId(null);
        dao.save(entityActor);
        return converter.convert(entityActor);
    }

    public ActorDto updateActor(ActorDto dto, Long id) {
        ActorDto result = null;
        if (actorExistById(id)) {
            ActorEntity entityActor = converter.convert(dto);
            entityActor.setId(id);
            result = converter.convert(dao.save(entityActor));
        }
        return result;
    }

    private Boolean actorExistById(Long id) {
        return id != null && dao.existsById(id);
    }

}
