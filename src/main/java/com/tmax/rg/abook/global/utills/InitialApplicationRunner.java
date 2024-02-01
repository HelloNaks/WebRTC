package com.tmax.rg.abook.global.utills;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialApplicationRunner implements ApplicationRunner {

    private final InitUtil initUtil;

    @Override
    public void run(ApplicationArguments args) {

        initUtil.init();

    }

}