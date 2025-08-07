ALTER TABLE topicos
ADD CONSTRAINT CK_topicos UNIQUE (titulo,mensaje);

