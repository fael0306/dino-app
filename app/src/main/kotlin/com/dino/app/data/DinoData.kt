package com.dino.app.data

import com.dino.app.data.model.*

object DinoData {
    val CLASSICOS: List<Dinosaur> = listOf(
        Dinosaur("Tyrannosaurus rex", "Cretáceo", "Carnívoro", 12.3, 4.0, 8.4, "Bípede"),
        Dinosaur("Triceratops", "Cretáceo", "Herbívoro", 9.0, 3.0, 6.0, "Quadrúpede"),
        Dinosaur("Velociraptor", "Cretáceo", "Carnívoro", 2.0, 0.5, 0.015, "Bípede"),
        Dinosaur("Brachiosaurus", "Jurássico", "Herbívoro", 25.0, 12.0, 50.0, "Quadrúpede"),
        Dinosaur("Stegosaurus", "Jurássico", "Herbívoro", 9.0, 4.0, 7.0, "Quadrúpede"),
        Dinosaur("Spinosaurus", "Cretáceo", "Piscívoro", 15.0, 5.0, 7.5, "Bípede"),
        Dinosaur("Patagotitan", "Cretáceo", "Herbívoro", 37.0, 8.0, 70.0, "Quadrúpede")
    )

    val REAIS: List<Dinosaur> = listOf(
        Dinosaur("Tyrannosaurus rex", "Cretáceo Superior", "Carnívoro", 12.3, 4.0, 8.4, "Bípede", "Mordida mais forte entre animais terrestres."),
        Dinosaur("Triceratops horridus", "Cretáceo Superior", "Herbívoro", 9.0, 3.0, 6.0, "Quadrúpede", "Três chifres e folho ósseo."),
        Dinosaur("Velociraptor mongoliensis", "Cretáceo Superior", "Carnívoro", 2.0, 0.5, 0.015, "Bípede", "Menor que nos filmes; tinha penas."),
        Dinosaur("Brachiosaurus altithorax", "Jurássico Superior", "Herbívoro", 25.0, 12.0, 50.0, "Quadrúpede", "Narinas no topo da cabeça."),
        Dinosaur("Stegosaurus stenops", "Jurássico Superior", "Herbívoro", 9.0, 4.0, 7.0, "Quadrúpede", "Placas ósseas e talo na cauda."),
        Dinosaur("Spinosaurus aegyptiacus", "Cretáceo Superior", "Piscívoro", 15.0, 5.0, 7.5, "Bípede", "Vela neural de 1,8 m."),
        Dinosaur("Patagotitan mayorum", "Cretáceo Inferior", "Herbívoro", 37.0, 8.0, 70.0, "Quadrúpede", "Um dos maiores animais terrestres."),
        Dinosaur("Allosaurus fragilis", "Jurássico Superior", "Carnívoro", 8.5, 3.0, 2.3, "Bípede", "Principal predador do Jurássico."),
        Dinosaur("Diplodocus longus", "Jurássico Superior", "Herbívoro", 27.0, 5.0, 15.0, "Quadrúpede", "Cauda de ~13 m."),
        Dinosaur("Ankylosaurus magniventris", "Cretáceo Superior", "Herbívoro", 6.0, 1.7, 6.0, "Quadrúpede", "Armadura óssea e clava na cauda."),
        Dinosaur("Parasaurolophus walkeri", "Cretáceo Superior", "Herbívoro", 9.5, 3.0, 2.5, "Quadrúpede", "Crista tubular oca."),
        Dinosaur("Pachycephalosaurus wyomingensis", "Cretáceo Superior", "Herbívoro", 4.5, 1.5, 0.45, "Bípede", "Domo ósseo de 25 cm."),
        Dinosaur("Carnotaurus sastrei", "Cretáceo Superior", "Carnívoro", 7.5, 3.0, 1.3, "Bípede", "Dois chifres sobre os olhos."),
        Dinosaur("Therizinosaurus cheloniformis", "Cretáceo Superior", "Herbívoro", 10.0, 4.5, 5.0, "Bípede", "Garras de até 1 m."),
        Dinosaur("Deinonychus antirrhopus", "Cretáceo Inferior", "Carnívoro", 3.4, 1.2, 0.073, "Bípede", "Garra em foice no pé."),
        Dinosaur("Iguanodon bernissartensis", "Cretáceo Inferior", "Herbívoro", 10.0, 3.0, 3.0, "Quadrúpede", "Polegar em forma de espigão."),
        Dinosaur("Giganotosaurus carolinii", "Cretáceo Superior", "Carnívoro", 13.0, 4.0, 8.0, "Bípede", "Rival do T. rex."),
        Dinosaur("Archaeopteryx lithographica", "Jurássico Superior", "Carnívoro", 0.5, 0.3, 0.001, "Bípede", "Elo entre dinossauros e aves."),
        Dinosaur("Apatosaurus louisae", "Jurássico Superior", "Herbívoro", 21.0, 5.0, 20.0, "Quadrúpede", "Pescoço curto e maciço."),
        Dinosaur("Ceratosaurus nasicornis", "Jurássico Superior", "Carnívoro", 6.0, 2.5, 1.0, "Bípede", "Chifre nasal.")
    )

    val QUIZ: Map<String, List<QuizQuestion>> = mapOf(
        "Fácil" to listOf(
            QuizQuestion("Qual dinossauro tinha três chifres?", listOf("T. rex", "Triceratops", "Estegossauro"), 1, "Triceratops."),
            QuizQuestion("Qual período veio primeiro?", listOf("Cretáceo", "Jurássico", "Triássico"), 2, "Triássico.")
        ),
        "Médio" to listOf(
            QuizQuestion("Qual era a dieta do Espinossauro?", listOf("Herbívoro", "Carnívoro", "Piscívoro"), 2, "Piscívoro.")
        ),
        "Difícil" to listOf(
            QuizQuestion("Quem sobreviveu ao K-Pg?", listOf("Todos", "Aves", "Pterossauros"), 1, "Aves.")
        )
    )

    val PERIODOS: List<Periodo> = listOf(
        Periodo("Triássico", 252, 201, 0xFF8e44ad, listOf("Pangeia")),
        Periodo("Jurássico", 201, 145, 0xFF16a085, listOf("Fragmentação de Pangeia")),
        Periodo("Cretáceo", 145, 66, 0xFFe67e22, listOf("Flores", "Extinção K-Pg"))
    )

    val CLIMA: Map<String, ClimaData> = mapOf(
        "Triássico (252-201 Ma)" to ClimaData(20.0, 1800, "Coníferas", "Alto", "Clima árido."),
        "Jurássico (201-145 Ma)" to ClimaData(22.0, 1400, "Cicadáceas", "Moderado", "Clima úmido."),
        "Cretáceo (145-66 Ma)" to ClimaData(24.0, 1000, "Angiospermas", "Alto", "Climas quentes.")
    )

    val COORDENADAS: Map<String, List<Coordenada>> = mapOf(
        "Tyrannosaurus rex" to listOf(Coordenada(47.0, -104.0)),
        "Triceratops horridus" to listOf(Coordenada(44.0, -105.0)),
        "Brachiosaurus altithorax" to listOf(Coordenada(39.0, -108.0)),
        "Stegosaurus stenops" to listOf(Coordenada(39.5, -105.5)),
        "Spinosaurus aegyptiacus" to listOf(Coordenada(31.0, 25.0))
    )

    val ICNOFOSSEIS: Map<String, Icnofossil> = mapOf(
        "Eubrontes" to Icnofossil("Eubrontes", 3, true, "grande", "alongada", null),
        "Grallator" to Icnofossil("Grallator", 3, false, "pequeno", null, null),
        "Anomoepus" to Icnofossil("Anomoepus", 4, false, null, null, "larga"),
        "Chirotherium" to Icnofossil("Chirotherium", 4, true, null, null, null)
    )

    val ARVORE_ARESTAS: List<Pair<String, String>> = listOf(
        "Reptilia" to "Archosauria", "Archosauria" to "Dinosauria",
        "Archosauria" to "Pterosauria", "Dinosauria" to "Saurischia",
        "Dinosauria" to "Ornithischia", "Saurischia" to "Theropoda",
        "Saurischia" to "Sauropodomorpha", "Theropoda" to "Tyrannosauridae",
        "Theropoda" to "Dromaeosauridae", "Theropoda" to "Spinosauridae",
        "Sauropodomorpha" to "Brachiosauridae", "Sauropodomorpha" to "Diplodocidae",
        "Ornithischia" to "Ceratopsia", "Ornithischia" to "Ornithopoda",
        "Ornithischia" to "Stegosauria", "Ornithischia" to "Ankylosauria"
    )
}