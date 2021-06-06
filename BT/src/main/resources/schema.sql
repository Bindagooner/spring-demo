CREATE table `user` (
    `id` varchar(36) PRIMARY KEY,
    `username` varchar(36) UNIQUE NOT NULL,
    `password` varchar(100) NOT NULL,
    `role` varchar(36) NOT NULL
);

CREATE table `book` (
    `id` varchar(36) PRIMARY KEY,
    `name` varchar(255),
    `author` varchar(100)
);
