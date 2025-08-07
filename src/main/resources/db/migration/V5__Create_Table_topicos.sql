create table topicos (
    id int not null AUTO_INCREMENT,
    titulo varchar(100)  not null,
    mensaje varchar(255)  not null,
    fecha_creacion DATETIME  not null DEFAULT NOW(),
    status varchar(50) not null,
    autor int not null,
    curso int not null,
    PRIMARY KEY (id),
    FOREIGN KEY (autor) REFERENCES usuarios(id),
    FOREIGN KEY (curso) REFERENCES cursos(id)
);