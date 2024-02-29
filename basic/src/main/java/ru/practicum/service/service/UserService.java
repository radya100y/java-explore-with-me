package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserIn;
import ru.practicum.model.user.UserMapper;
import ru.practicum.model.user.UserOut;
import ru.practicum.error.NotFoundException;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public UserOut saveTransactional(UserIn userIn) {
        return UserMapper.toUserOut(userRepository.save(UserMapper.toUser(userIn)));
    }

    public UserOut saveAndReturn(UserIn user) {
        try {
            return saveTransactional(user);
        } catch (DataIntegrityViolationException exc) {
            throw new ConflictException("Пользователь с адресом " + user.getEmail() + " уже существует");
        }
    }
    public UserOut add(UserIn userIn) {
        try {
            return UserMapper.toUserOut(userRepository.save(UserMapper.toUser(userIn)));
        } catch (DataIntegrityViolationException exc) {
            throw new ConflictException("Пользователь с адресом " + userIn.getEmail() + " уже существует");
        }
    }

    public UserOut delete(long id) {
        UserOut user = get(id);
        userRepository.deleteById(id);
        return user;
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public UserOut get(long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserOut)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден"));
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public List<UserOut> gets(List<Long> ids, Pageable reqPage) {

        if (ids.size() == 0) {
            return userRepository.getAllLimitNoQueringMethod(reqPage).stream()
                    .map(UserMapper::toUserOut)
                    .collect(Collectors.toList());
        }

        return userRepository.getAllUserContainsAndLimitNotQueringMethod(ids, reqPage).stream()
                .map(UserMapper::toUserOut)
                .collect(Collectors.toList());
    }

}
