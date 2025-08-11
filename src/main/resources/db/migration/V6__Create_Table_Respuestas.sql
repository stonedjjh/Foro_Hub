create table respuestas (
    id int not null AUTO_INCREMENT,
    mensaje varchar(255)  not null,
    topico int not null,
    fecha_creacion DATETIME  not null DEFAULT NOW(),
    autor int not null,
    solucion BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (topico) REFERENCES topicos(id),
    FOREIGN KEY (autor) REFERENCES usuarios(id)
);