package com.fiap.brinquedos.service;


import com.fiap.brinquedos.entity.Brinquedo;
import com.fiap.brinquedos.repository.BrinquedoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BrinquedoService {

    @Autowired
    private BrinquedoRepository brinquedoRepository;

    public List<Brinquedo> listarTodos() {
        return brinquedoRepository.findAll();
    }

    public Optional<Brinquedo> buscarPorId (Long Id) {
        return brinquedoRepository.findById(Id);
    }

    public Brinquedo salvar (Brinquedo brinquedo) {
        return brinquedoRepository.save(brinquedo);
    }

    public void deletar (Long Id) {
        brinquedoRepository.deleteById(Id);
    }

    public Brinquedo atualizar (Brinquedo brinquedo, Long id) {
        return brinquedoRepository.findById(id)
                .map(brinquedoExistente -> {
                    brinquedoExistente.setBrinquedoNm(brinquedo.getBrinquedoNm());
                    brinquedoExistente.setBrinquedoTp(brinquedo.getBrinquedoTp());
                    brinquedoExistente.setBrinquedoClassificacao(brinquedo.getBrinquedoClassificacao());
                    brinquedoExistente.setBrinquedoPreco(brinquedo.getBrinquedoPreco());
                    brinquedoExistente.setBrinquedoTam(brinquedo.getBrinquedoTam());

                    return brinquedoRepository.save(brinquedoExistente);
                }).orElseThrow(() -> new IllegalStateException("Brinquedo com ID " + id + " nao existe"));
    }

    public Brinquedo atualizarParcialmente(Long id, Map<String, Object> updates){
        return brinquedoRepository.findById(id)
                .map(brinquedoExistente -> {
                    updates.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(Brinquedo.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, brinquedoExistente, value );
                        }
                    });
                    return brinquedoRepository.save(brinquedoExistente);
                }).orElseThrow(() -> new IllegalArgumentException("Brinquedo com ID " + id + " nao existe."));
    }
}
