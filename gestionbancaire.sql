-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 27 nov. 2021 à 00:29
-- Version du serveur :  10.4.17-MariaDB
-- Version de PHP : 7.4.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionbancaire`
--

-- --------------------------------------------------------

--
-- Structure de la table `banquier`
--

CREATE TABLE `banquier` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `banquier`
--

INSERT INTO `banquier` (`id`, `nom`) VALUES
(1, 'banquier1'),
(2, 'banquier2'),
(3, 'banquier3'),
(4, 'banquier4'),
(5, 'banquier5'),
(6, 'banquier6'),
(7, 'banquier7'),
(8, 'banquier8'),
(9, 'banquier9'),
(10, 'banquier10');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `login` varchar(255) NOT NULL,
  `mdp` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `login`, `mdp`) VALUES
(1, 'user1', 'user1'),
(2, 'user2', 'user2'),
(3, 'user3', 'user3'),
(4, 'user4', 'user4'),
(5, 'user5', 'user5'),
(6, 'user6', 'user6'),
(7, 'user7', 'user7'),
(8, 'user8', 'user8'),
(9, 'user9', 'user9'),
(10, 'user10', 'user10');

-- --------------------------------------------------------

--
-- Structure de la table `compte`
--

CREATE TABLE `compte` (
  `id` int(11) NOT NULL,
  `solde` int(11) NOT NULL,
  `client` int(11) NOT NULL,
  `banquier` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `compte`
--

INSERT INTO `compte` (`id`, `solde`, `client`, `banquier`) VALUES
(1, 500, 1, 1),
(2, 4865, 2, 1),
(3, 52025, 3, 2),
(4, -52, 4, 2),
(5, 852, 5, 3),
(6, 1545, 6, 6),
(7, 8754, 7, 7),
(8, 9630, 8, 8),
(9, 4256, 9, 10),
(10, 75, 10, 1);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `banquier`
--
ALTER TABLE `banquier`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `compte`
--
ALTER TABLE `compte`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `fk_clientT` (`client`),
  ADD KEY `fk_banquier` (`banquier`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `banquier`
--
ALTER TABLE `banquier`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `compte`
--
ALTER TABLE `compte`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `compte`
--
ALTER TABLE `compte`
  ADD CONSTRAINT `fk_banquier` FOREIGN KEY (`banquier`) REFERENCES `banquier` (`id`),
  ADD CONSTRAINT `fk_clientT` FOREIGN KEY (`client`) REFERENCES `client` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
