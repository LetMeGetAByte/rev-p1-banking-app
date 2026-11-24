package com.lmgbgpatr.model;

import java.time.OffsetDateTime;

public record Log(int id, String sub, String desc, String[] tags, OffsetDateTime timestamp) {}