create table respuesta (
    id int not null AUTO_INCREMENT,
    mensaje varchar(255)  not null,
    topico int not null,
    fecha_creacion DATETIME  not null DEFAULT NOW(),
    autor int not null,
    solucion varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (topico) REFERENCES topico(id),
    FOREIGN KEY (autor) REFERENCES usuario(id)
);