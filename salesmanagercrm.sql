-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : Dim 20 juin 2021 à 13:43
-- Version du serveur :  5.7.24
-- Version de PHP : 7.2.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `salesmanagercrm`
--

-- --------------------------------------------------------

--
-- Structure de la table `addresses`
--

CREATE TABLE `addresses`
(
    `ID`           int(11) UNSIGNED              NOT NULL,
    `ID_Cities`    int(11) UNSIGNED              NOT NULL,
    `ID_Contacts`  int(11) UNSIGNED              DEFAULT NULL,
    `ID_Companies` int(11) UNSIGNED              DEFAULT NULL,
    `Street`       varchar(255) COLLATE utf8_bin NOT NULL,
    `Number`       varchar(255) COLLATE utf8_bin NOT NULL,
    `Box`          varchar(255) COLLATE utf8_bin DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `branch_activities`
--

CREATE TABLE `branch_activities`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `cities`
--

CREATE TABLE `cities`
(
    `ID`           int(11) UNSIGNED              NOT NULL,
    `ID_Countries` int(11) UNSIGNED              NOT NULL,
    `Region`       varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Postal_Code`  varchar(255) COLLATE utf8_bin NOT NULL,
    `Label`        varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `civilities`
--

CREATE TABLE `civilities`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `civilities`
--

INSERT INTO `civilities` (`ID`, `Label`)
VALUES (1, 'Mr'),
       (2, 'Mme');

-- --------------------------------------------------------

--
-- Structure de la table `companies`
--

CREATE TABLE `companies`
(
    `ID`                   int(11) UNSIGNED              NOT NULL,
    `ID_Users`             int(11) UNSIGNED              NOT NULL,
    `ID_Company_Types`     int(11) UNSIGNED              NOT NULL,
    `ID_Branch_Activities` int(11) UNSIGNED              DEFAULT NULL,
    `Label`                varchar(255) COLLATE utf8_bin NOT NULL,
    `Description`          varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `VAT_Number`           varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Bank_Account`         varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Domain_Name`          varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Employees_Number`     int(11)                       DEFAULT NULL,
    `Modification_Date`    datetime                      DEFAULT NULL,
    `Register_Date`        datetime                      NOT NULL,
    `Annual_Sales`         double                        DEFAULT NULL,
    `Revenue`              double                        DEFAULT NULL,
    `LinkedIn_Page`        varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Creation_Date`        datetime                      DEFAULT NULL,
    `Closing_Date`         datetime                      DEFAULT NULL,
    `Phone_Number`         varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Email`                varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Website`              varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `IsActive`             tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `companies_contacts`
--

CREATE TABLE `companies_contacts`
(
    `ID`           int(11) UNSIGNED NOT NULL,
    `ID_Companies` int(11) UNSIGNED NOT NULL,
    `ID_Contacts`  int(11) UNSIGNED NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `company_types`
--

CREATE TABLE `company_types`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `company_types`
--

INSERT INTO `company_types` (`ID`, `Label`)
VALUES (1, 'Prospect'),
       (2, 'Partenaire'),
       (3, 'Revendeur'),
       (4, 'Fournisseur'),
       (5, 'Autre');

-- --------------------------------------------------------

--
-- Structure de la table `contacts`
--

CREATE TABLE `contacts`
(
    `ID`                   int(11) UNSIGNED              NOT NULL,
    `ID_Users`             int(11) UNSIGNED              NOT NULL,
    `ID_Civilities`        int(11) UNSIGNED              DEFAULT NULL,
    `ID_Job_Titles`        int(11) UNSIGNED              DEFAULT NULL,
    `ID_Contact_Types`     int(11) UNSIGNED              NOT NULL,
    `ID_Branch_Activities` int(11) UNSIGNED              DEFAULT NULL,
    `Lastname`             varchar(255) COLLATE utf8_bin NOT NULL,
    `Firstname`            varchar(255) COLLATE utf8_bin NOT NULL,
    `Bank_Account`         varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Email`                varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `Register_Date`        datetime                      NOT NULL,
    `Modification_Date`    datetime                      DEFAULT NULL,
    `Phone_Number`         varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `IsActive`             tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `contact_types`
--

CREATE TABLE `contact_types`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `contact_types`
--

INSERT INTO `contact_types` (`ID`, `Label`)
VALUES (1, 'Prospect'),
       (2, 'Client');

-- --------------------------------------------------------

--
-- Structure de la table `conversations`
--

CREATE TABLE `conversations`
(
    `ID`            int(11) UNSIGNED              NOT NULL,
    `ID_Users`      int(11) UNSIGNED              NOT NULL,
    `Creation_Date` datetime                      NOT NULL,
    `Message`       varchar(255) COLLATE utf8_bin NOT NULL,
    `IsActive`      tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `countries`
--

CREATE TABLE `countries`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `job_titles`
--

CREATE TABLE `job_titles`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `notes`
--

CREATE TABLE `notes`
(
    `ID`            int(11) UNSIGNED              NOT NULL,
    `ID_Users`      int(11) UNSIGNED              NOT NULL,
    `ID_Contacts`   int(11) UNSIGNED DEFAULT NULL,
    `ID_Companies`  int(11) UNSIGNED DEFAULT NULL,
    `Creation_Date` datetime                      NOT NULL,
    `Message`       varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `permissions`
--

CREATE TABLE `permissions`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `roles`
--

CREATE TABLE `roles`
(
    `ID`       int(11) UNSIGNED              NOT NULL,
    `Label`    varchar(255) COLLATE utf8_bin NOT NULL,
    `IsActive` tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `roles`
--

INSERT INTO `roles` (`ID`, `Label`, `IsActive`)
VALUES (1, 'Admin', 1),
       (2, 'Utilisateur', 1);

-- --------------------------------------------------------

--
-- Structure de la table `roles_permissions`
--

CREATE TABLE `roles_permissions`
(
    `ID`             int(11) UNSIGNED NOT NULL,
    `ID_Roles`       int(11) UNSIGNED NOT NULL,
    `ID_Permissions` int(11) UNSIGNED NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `tasks`
--

CREATE TABLE `tasks`
(
    `ID`            int(11) UNSIGNED              NOT NULL,
    `ID_Users`      int(11) UNSIGNED              NOT NULL,
    `ID_Contacts`   int(11) UNSIGNED                                       DEFAULT NULL,
    `ID_Companies`  int(11) UNSIGNED                                       DEFAULT NULL,
    `ID_Task_Types` int(11) UNSIGNED                                       DEFAULT NULL,
    `Title`         varchar(255) COLLATE utf8_bin NOT NULL,
    `Priority`      enum ('Elevée','Moyenne','Faible','') COLLATE utf8_bin DEFAULT NULL,
    `Creation_Date` datetime                      NOT NULL,
    `End_Date`      datetime                                               DEFAULT NULL,
    `Description`   varchar(255) COLLATE utf8_bin                          DEFAULT NULL,
    `Status`        tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `task_types`
--

CREATE TABLE `task_types`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `task_types`
--

INSERT INTO `task_types` (`ID`, `Label`)
VALUES (1, 'Appel'),
       (2, 'Email'),
       (3, 'A faire');

-- --------------------------------------------------------

--
-- Structure de la table `transactions`
--

CREATE TABLE `transactions`
(
    `ID`                    int(11) UNSIGNED              NOT NULL,
    `ID_Users`              int(11) UNSIGNED              NOT NULL,
    `ID_Contacts`           int(11) UNSIGNED DEFAULT NULL,
    `ID_Companies`          int(11) UNSIGNED DEFAULT NULL,
    `ID_Transaction_Types`  int(11) UNSIGNED DEFAULT NULL,
    `ID_Transaction_Phases` int(11) UNSIGNED DEFAULT NULL,
    `Title`                 varchar(255) COLLATE utf8_bin NOT NULL,
    `Amount`                double UNSIGNED  DEFAULT NULL,
    `Creation_Date`         datetime                      NOT NULL,
    `End_Date`              datetime         DEFAULT NULL,
    `IsActive`              tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `transaction_histories`
--

CREATE TABLE `transaction_histories`
(
    `ID`                    int(11) UNSIGNED NOT NULL,
    `ID_Transactions`       int(11) UNSIGNED NOT NULL,
    `ID_Transaction_Phases` int(11) UNSIGNED DEFAULT NULL,
    `Save_Date`             datetime         NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `transaction_phases`
--

CREATE TABLE `transaction_phases`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `transaction_phases`
--

INSERT INTO `transaction_phases` (`ID`, `Label`)
VALUES (1, 'Prospection'),
       (2, 'Qualification'),
       (3, 'Proposition'),
       (4, 'Négociation'),
       (5, 'Conclue'),
       (6, 'Annulé');

-- --------------------------------------------------------

--
-- Structure de la table `transaction_types`
--

CREATE TABLE `transaction_types`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `transaction_types`
--

INSERT INTO `transaction_types` (`ID`, `Label`)
VALUES (1, 'Nouvelle entreprise'),
       (2, 'Entreprise existante');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users`
(
    `ID`            int(11) UNSIGNED              NOT NULL,
    `ID_Roles`      int(11) UNSIGNED              NOT NULL,
    `Firstname`     varchar(255) COLLATE utf8_bin NOT NULL,
    `Lastname`      varchar(255) COLLATE utf8_bin NOT NULL,
    `Username`      varchar(255) COLLATE utf8_bin NOT NULL,
    `Password`      varchar(255) COLLATE utf8_bin NOT NULL,
    `Email`         varchar(255) COLLATE utf8_bin NOT NULL,
    `Register_Date` datetime                      NOT NULL,
    `IsActive`      tinyint(1)                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`ID`, `ID_Roles`, `Firstname`, `Lastname`, `Username`, `Password`, `Email`, `Register_Date`,
                     `IsActive`)
VALUES (1, 1, 'Admin', 'Admin', 'Admin', 'Admin', 'Admin@test.com', '2021-05-15 14:38:10', 1);

-- --------------------------------------------------------

--
-- Structure de la table `vouchers`
--

CREATE TABLE `vouchers`
(
    `ID`                int(11) UNSIGNED              NOT NULL,
    `ID_Users`          int(11) UNSIGNED              NOT NULL,
    `ID_Contacts`       int(11) UNSIGNED                                       DEFAULT NULL,
    `ID_Companies`      int(11) UNSIGNED                                       DEFAULT NULL,
    `ID_Voucher_Status` int(11) UNSIGNED              NOT NULL,
    `Title`             varchar(255) COLLATE utf8_bin NOT NULL,
    `Description`       varchar(255) COLLATE utf8_bin                          DEFAULT NULL,
    `Priority`          enum ('Elevée','Moyenne','Faible','') COLLATE utf8_bin DEFAULT NULL,
    `Creation_Date`     datetime                      NOT NULL,
    `End_Date`          datetime                                               DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `voucher_histories`
--

CREATE TABLE `voucher_histories`
(
    `ID`                int(11) UNSIGNED NOT NULL,
    `ID_Vouchers`       int(11) UNSIGNED NOT NULL,
    `ID_Voucher_Status` int(11) UNSIGNED NOT NULL,
    `Save_Date`         datetime         NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `voucher_status`
--

CREATE TABLE `voucher_status`
(
    `ID`    int(11) UNSIGNED              NOT NULL,
    `Label` varchar(255) COLLATE utf8_bin NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

--
-- Déchargement des données de la table `voucher_status`
--

INSERT INTO `voucher_status` (`ID`, `Label`)
VALUES (1, 'En attente de contact'),
       (2, 'En attente'),
       (3, 'Fermé');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `addresses`
--
ALTER TABLE `addresses`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Cities` (`ID_Cities`),
    ADD KEY `ID_Contacts` (`ID_Contacts`),
    ADD KEY `ID_Companies` (`ID_Companies`) USING BTREE;

--
-- Index pour la table `branch_activities`
--
ALTER TABLE `branch_activities`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `cities`
--
ALTER TABLE `cities`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Countries` (`ID_Countries`);

--
-- Index pour la table `civilities`
--
ALTER TABLE `civilities`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `companies`
--
ALTER TABLE `companies`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Branch_Activities` (`ID_Branch_Activities`),
    ADD KEY `ID_Users` (`ID_Users`),
    ADD KEY `ID_Company_Types` (`ID_Company_Types`) USING BTREE;

--
-- Index pour la table `companies_contacts`
--
ALTER TABLE `companies_contacts`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Contacts` (`ID_Contacts`),
    ADD KEY `ID_Companies` (`ID_Companies`) USING BTREE;

--
-- Index pour la table `company_types`
--
ALTER TABLE `company_types`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `contacts`
--
ALTER TABLE `contacts`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Branch_Activities` (`ID_Branch_Activities`),
    ADD KEY `ID_Civilities` (`ID_Civilities`),
    ADD KEY `ID_Contact_Types` (`ID_Contact_Types`),
    ADD KEY `ID_Job_Titles` (`ID_Job_Titles`),
    ADD KEY `ID_Users` (`ID_Users`);

--
-- Index pour la table `contact_types`
--
ALTER TABLE `contact_types`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `conversations`
--
ALTER TABLE `conversations`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Users` (`ID_Users`);

--
-- Index pour la table `countries`
--
ALTER TABLE `countries`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `job_titles`
--
ALTER TABLE `job_titles`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `notes`
--
ALTER TABLE `notes`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_ Contacts` (`ID_Contacts`),
    ADD KEY `ID_Users` (`ID_Users`),
    ADD KEY `ID_ Companies` (`ID_Companies`) USING BTREE;

--
-- Index pour la table `permissions`
--
ALTER TABLE `permissions`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `Label` (`Label`);

--
-- Index pour la table `roles`
--
ALTER TABLE `roles`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `Label` (`Label`);

--
-- Index pour la table `roles_permissions`
--
ALTER TABLE `roles_permissions`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Permissions` (`ID_Permissions`),
    ADD KEY `ID_Roles` (`ID_Roles`);

--
-- Index pour la table `tasks`
--
ALTER TABLE `tasks`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Contacts` (`ID_Contacts`),
    ADD KEY `ID_Users` (`ID_Users`),
    ADD KEY `ID_Task_Types` (`ID_Task_Types`),
    ADD KEY `ID_Companies` (`ID_Companies`) USING BTREE;

--
-- Index pour la table `task_types`
--
ALTER TABLE `task_types`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `transactions`
--
ALTER TABLE `transactions`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Contacts` (`ID_Contacts`),
    ADD KEY `ID_Transaction_Phases` (`ID_Transaction_Phases`),
    ADD KEY `ID_Transaction_Types` (`ID_Transaction_Types`),
    ADD KEY `ID_Users` (`ID_Users`),
    ADD KEY `ID_Companies` (`ID_Companies`) USING BTREE;

--
-- Index pour la table `transaction_histories`
--
ALTER TABLE `transaction_histories`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Transactions` (`ID_Transactions`),
    ADD KEY `ID_Transaction_Phases` (`ID_Transaction_Phases`);

--
-- Index pour la table `transaction_phases`
--
ALTER TABLE `transaction_phases`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `transaction_types`
--
ALTER TABLE `transaction_types`
    ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`ID`),
    ADD UNIQUE KEY `Username` (`Username`),
    ADD KEY `ID_Roles` (`ID_Roles`);

--
-- Index pour la table `vouchers`
--
ALTER TABLE `vouchers`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Contacts` (`ID_Contacts`),
    ADD KEY `ID_Users` (`ID_Users`),
    ADD KEY `ID_Voucher_Status` (`ID_Voucher_Status`),
    ADD KEY `ID_Companies` (`ID_Companies`) USING BTREE;

--
-- Index pour la table `voucher_histories`
--
ALTER TABLE `voucher_histories`
    ADD PRIMARY KEY (`ID`),
    ADD KEY `ID_Vouchers` (`ID_Vouchers`),
    ADD KEY `ID_Voucher_Status` (`ID_Voucher_Status`);

--
-- Index pour la table `voucher_status`
--
ALTER TABLE `voucher_status`
    ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `addresses`
--
ALTER TABLE `addresses`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `branch_activities`
--
ALTER TABLE `branch_activities`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `cities`
--
ALTER TABLE `cities`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `civilities`
--
ALTER TABLE `civilities`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT pour la table `companies`
--
ALTER TABLE `companies`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `companies_contacts`
--
ALTER TABLE `companies_contacts`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `company_types`
--
ALTER TABLE `company_types`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 6;

--
-- AUTO_INCREMENT pour la table `contacts`
--
ALTER TABLE `contacts`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `contact_types`
--
ALTER TABLE `contact_types`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 6;

--
-- AUTO_INCREMENT pour la table `conversations`
--
ALTER TABLE `conversations`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `countries`
--
ALTER TABLE `countries`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `job_titles`
--
ALTER TABLE `job_titles`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `notes`
--
ALTER TABLE `notes`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `permissions`
--
ALTER TABLE `permissions`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `roles`
--
ALTER TABLE `roles`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT pour la table `roles_permissions`
--
ALTER TABLE `roles_permissions`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `tasks`
--
ALTER TABLE `tasks`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `task_types`
--
ALTER TABLE `task_types`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 5;

--
-- AUTO_INCREMENT pour la table `transactions`
--
ALTER TABLE `transactions`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `transaction_histories`
--
ALTER TABLE `transaction_histories`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `transaction_phases`
--
ALTER TABLE `transaction_phases`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 7;

--
-- AUTO_INCREMENT pour la table `transaction_types`
--
ALTER TABLE `transaction_types`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT pour la table `vouchers`
--
ALTER TABLE `vouchers`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `voucher_histories`
--
ALTER TABLE `voucher_histories`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `voucher_status`
--
ALTER TABLE `voucher_status`
    MODIFY `ID` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 4;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `addresses`
--
ALTER TABLE `addresses`
    ADD CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`ID_Cities`) REFERENCES `cities` (`ID`),
    ADD CONSTRAINT `addresses_ibfk_2` FOREIGN KEY (`ID_Companies`) REFERENCES `companies` (`ID`),
    ADD CONSTRAINT `addresses_ibfk_3` FOREIGN KEY (`ID_Contacts`) REFERENCES `contacts` (`ID`);

--
-- Contraintes pour la table `cities`
--
ALTER TABLE `cities`
    ADD CONSTRAINT `cities_ibfk_1` FOREIGN KEY (`ID_Countries`) REFERENCES `countries` (`ID`);

--
-- Contraintes pour la table `companies`
--
ALTER TABLE `companies`
    ADD CONSTRAINT `companies_ibfk_1` FOREIGN KEY (`ID_Branch_Activities`) REFERENCES `branch_activities` (`ID`),
    ADD CONSTRAINT `companies_ibfk_2` FOREIGN KEY (`ID_Company_Types`) REFERENCES `company_types` (`ID`),
    ADD CONSTRAINT `companies_ibfk_3` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`);

--
-- Contraintes pour la table `companies_contacts`
--
ALTER TABLE `companies_contacts`
    ADD CONSTRAINT `companies_contacts_ibfk_1` FOREIGN KEY (`ID_Companies`) REFERENCES `companies` (`ID`),
    ADD CONSTRAINT `companies_contacts_ibfk_2` FOREIGN KEY (`ID_Contacts`) REFERENCES `contacts` (`ID`);

--
-- Contraintes pour la table `contacts`
--
ALTER TABLE `contacts`
    ADD CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`ID_Branch_Activities`) REFERENCES `branch_activities` (`ID`),
    ADD CONSTRAINT `contacts_ibfk_2` FOREIGN KEY (`ID_Civilities`) REFERENCES `civilities` (`ID`),
    ADD CONSTRAINT `contacts_ibfk_3` FOREIGN KEY (`ID_Contact_Types`) REFERENCES `contact_types` (`ID`),
    ADD CONSTRAINT `contacts_ibfk_4` FOREIGN KEY (`ID_Job_Titles`) REFERENCES `job_titles` (`ID`),
    ADD CONSTRAINT `contacts_ibfk_5` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`);

--
-- Contraintes pour la table `conversations`
--
ALTER TABLE `conversations`
    ADD CONSTRAINT `conversations_ibfk_1` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`);

--
-- Contraintes pour la table `notes`
--
ALTER TABLE `notes`
    ADD CONSTRAINT `notes_ibfk_1` FOREIGN KEY (`ID_Companies`) REFERENCES `companies` (`ID`),
    ADD CONSTRAINT `notes_ibfk_3` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`),
    ADD CONSTRAINT `notes_ibfk_4` FOREIGN KEY (`ID_Contacts`) REFERENCES `contacts` (`ID`);

--
-- Contraintes pour la table `roles_permissions`
--
ALTER TABLE `roles_permissions`
    ADD CONSTRAINT `roles_permissions_ibfk_1` FOREIGN KEY (`ID_Permissions`) REFERENCES `permissions` (`ID`),
    ADD CONSTRAINT `roles_permissions_ibfk_2` FOREIGN KEY (`ID_Roles`) REFERENCES `roles` (`ID`);

--
-- Contraintes pour la table `tasks`
--
ALTER TABLE `tasks`
    ADD CONSTRAINT `tasks_ibfk_1` FOREIGN KEY (`ID_Companies`) REFERENCES `companies` (`ID`),
    ADD CONSTRAINT `tasks_ibfk_3` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`),
    ADD CONSTRAINT `tasks_ibfk_4` FOREIGN KEY (`ID_Task_Types`) REFERENCES `task_types` (`ID`),
    ADD CONSTRAINT `tasks_ibfk_5` FOREIGN KEY (`ID_Contacts`) REFERENCES `contacts` (`ID`);

--
-- Contraintes pour la table `transactions`
--
ALTER TABLE `transactions`
    ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`ID_Companies`) REFERENCES `companies` (`ID`),
    ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`ID_Transaction_Phases`) REFERENCES `transaction_phases` (`ID`),
    ADD CONSTRAINT `transactions_ibfk_4` FOREIGN KEY (`ID_Transaction_Types`) REFERENCES `transaction_types` (`ID`),
    ADD CONSTRAINT `transactions_ibfk_5` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`),
    ADD CONSTRAINT `transactions_ibfk_6` FOREIGN KEY (`ID_Contacts`) REFERENCES `contacts` (`ID`);

--
-- Contraintes pour la table `transaction_histories`
--
ALTER TABLE `transaction_histories`
    ADD CONSTRAINT `transaction_histories_ibfk_1` FOREIGN KEY (`ID_Transactions`) REFERENCES `transactions` (`ID`),
    ADD CONSTRAINT `transaction_histories_ibfk_2` FOREIGN KEY (`ID_Transaction_Phases`) REFERENCES `transaction_phases` (`ID`);

--
-- Contraintes pour la table `users`
--
ALTER TABLE `users`
    ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`ID_Roles`) REFERENCES `roles` (`ID`);

--
-- Contraintes pour la table `vouchers`
--
ALTER TABLE `vouchers`
    ADD CONSTRAINT `vouchers_ibfk_1` FOREIGN KEY (`ID_Companies`) REFERENCES `companies` (`ID`),
    ADD CONSTRAINT `vouchers_ibfk_3` FOREIGN KEY (`ID_Users`) REFERENCES `users` (`ID`),
    ADD CONSTRAINT `vouchers_ibfk_4` FOREIGN KEY (`ID_Voucher_Status`) REFERENCES `voucher_status` (`ID`),
    ADD CONSTRAINT `vouchers_ibfk_5` FOREIGN KEY (`ID_Contacts`) REFERENCES `contacts` (`ID`);

--
-- Contraintes pour la table `voucher_histories`
--
ALTER TABLE `voucher_histories`
    ADD CONSTRAINT `voucher_histories_ibfk_1` FOREIGN KEY (`ID_Vouchers`) REFERENCES `vouchers` (`ID`),
    ADD CONSTRAINT `voucher_histories_ibfk_2` FOREIGN KEY (`ID_Voucher_Status`) REFERENCES `voucher_status` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
