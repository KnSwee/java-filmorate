package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mapper.MpaDtoMapper;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.exception.DuplicateEntityException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectDataException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage storage;

    public Mpa getMpaByFilm(Long filmId) {
        return storage.getMpa(filmId);
    }

    public Mpa getMpa(Long mpaId) {
        if (!storage.isMpaExistId(mpaId)) {
            throw new EntityNotFoundException("Mpa с id %s не найден".formatted(mpaId));
        }
        return storage.getMpaById(mpaId);
    }

    public boolean addMpa(MpaDto dto, Long filmId) {
        if (!storage.isMpaExistId(dto.getId())) {
            throw new IncorrectDataException("Mpa с id %s не сущетсвует".formatted(dto.getId()));
        }
        return storage.addMpa(MpaDtoMapper.mapToMpa(dto), filmId);
    }

    public Collection<MpaDto> findAll() {
        return storage.findAll().stream().map(MpaDtoMapper::mapToMpaDto).toList();
    }

    public MpaDto update(MpaDto dto) {
        if (storage.isMpaExist(dto.getName())) {
            throw new DuplicateEntityException("Рейтинг %s уже существует".formatted(dto.getName()));
        }
        return MpaDtoMapper.mapToMpaDto(storage.update(MpaDtoMapper.mapToMpa(dto)));
    }

    public boolean isMpaExistName(MpaDto dto) {
        return storage.isMpaExist(dto.getName());
    }

    public boolean isMpaExistId(MpaDto dto) {
        return storage.isMpaExistId(dto.getId());
    }

    public boolean delete(Long id) {
        return storage.delete(id);
    }
}
