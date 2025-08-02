-- Asignar el perfil 'ADMINISTRADOR' al usuario 'stone_djjh'
-- utilizando subconsultas para obtener los IDs dinámicamente.

INSERT INTO perfil_usuario (id_perfil, id_usuario) VALUES (
    (SELECT id FROM perfil WHERE nombre = 'ADMINISTRADOR'),
    (SELECT id FROM usuario WHERE correo_electronico = 'admin@admin.com')
);

INSERT INTO perfil_usuario (id_perfil, id_usuario) VALUES (
    (SELECT id FROM perfil WHERE nombre = 'USUARIO'),
    (SELECT id FROM usuario WHERE correo_electronico = 'test@test.com')
);
