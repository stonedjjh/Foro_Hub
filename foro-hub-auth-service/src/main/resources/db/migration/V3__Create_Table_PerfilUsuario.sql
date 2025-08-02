create table perfil_usuario (
    id_perfil  int not null,
    id_usuario int not null,
    CONSTRAINT PK_PerfilUsuario PRIMARY KEY (id_perfil,id_usuario),
    FOREIGN KEY (id_perfil) REFERENCES perfiles(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);