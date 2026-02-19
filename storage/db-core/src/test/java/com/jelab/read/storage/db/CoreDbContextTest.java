package com.jelab.read.storage.db;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@ActiveProfiles("local")
@Tag("context")
@SpringBootTest
@Disabled
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class CoreDbContextTest {

}
