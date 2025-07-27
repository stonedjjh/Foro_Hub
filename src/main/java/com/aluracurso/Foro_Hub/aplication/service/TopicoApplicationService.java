package com.aluracurso.Foro_Hub.aplication.service;

import com.aluracurso.Foro_Hub.domain.entity.Topico;
import com.aluracurso.Foro_Hub.domain.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicoApplicationService {
    @Autowired
    private TopicoRepository topicoRepository;

    public boolean persistirRepositorio(Topico topico){

        try {
            topicoRepository.save(topico);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
