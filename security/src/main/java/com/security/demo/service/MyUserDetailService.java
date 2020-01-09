package com.security.demo.service;

import com.security.demo.entity.Role;
import com.security.demo.entity.User;
import com.security.demo.mapper.RoleMapper;
import com.security.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(userName);
        if (null != user) {
            List<Role> roles = roleMapper.getRolesByUserId( user.getId() );
            user.setAuthorities( roles );
        }
        return user;
    }
}
