package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
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
    public UserOut add(UserIn userIn) {
        try {
            return UserMapper.toUserOut(userRepository.saveAndFlush(UserMapper.toUser(userIn)));
        } catch (DataIntegrityViolationException exc) {
            throw new ConflictException("Пользователь с адресом " + userIn.getEmail() + " уже существует");
        }
    }

    @Transactional
    public UserOut delete(long id) {
        UserOut user = get(id);
        userRepository.deleteById(id);
        userRepository.flush();
        return user;
    }

    public UserOut get(long id) {
        return userRepository.findById(id)
                .map(UserMapper::toUserOut)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден"));
    }

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
