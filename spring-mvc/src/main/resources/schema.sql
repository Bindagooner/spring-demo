CREATE table `user` (
    `id` varchar(36) PRIMARY KEY,
    `username` varchar(36) UNIQUE NOT NULL,
    `password` varchar(100) NOT NULL,
    `role` varchar(36) NOT NULL
);
