package ru.practicum.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.admin.model.UserIn;
import ru.practicum.admin.model.UserMapper;
import ru.practicum.admin.model.UserOut;
import ru.practicum.error.AlreadyExistException;
import ru.practicum.error.NotFoundException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public UserOut add(UserIn userIn) {
        try {
            return UserMapper.toUserOut(userRepository.save(UserMapper.toUser(userIn)));
        } catch (DataIntegrityViolationException exc) {
            throw new AlreadyExistException("Пользователь с адресом " + userIn.getEmail() + " уже существует");
        }
    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public UserOut get(long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserOut)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден"));
    }
}
