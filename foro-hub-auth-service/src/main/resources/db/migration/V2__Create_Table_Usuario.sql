create table usuarios (
    id int not null AUTO_INCREMENT,
    nombre varchar(100)  not null,
    correo_electronico varchar(100) unique not null,
    contraseña varchar(255) not null,
    PRIMARY KEY (id)
);
