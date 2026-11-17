package com.dino.app.data

import com.dino.app.data.model.QuizQuestion

object QuizData {
    val PERGUNTAS: Map<String, List<QuizQuestion>> = mapOf(
        "Fácil" to listOf(
            QuizQuestion("Qual dinossauro tinha três chifres?", listOf("T. rex", "Triceratops", "Estegossauro"), 1, "Triceratops."),
            QuizQuestion("Qual período veio primeiro?", listOf("Cretáceo", "Jurássico", "Triássico"), 2, "O Triássico veio antes do Jurássico e do Cretáceo."),
            QuizQuestion("Qual destes tinha placas ósseas nas costas?", listOf("Estegossauro", "Triceratops", "Velociraptor"), 0, "O Stegosaurus tinha placas ósseas dorsais."),
            QuizQuestion("Os dinossauros dominaram qual era?", listOf("Paleozoica", "Mesozoica", "Cenozoica"), 1, "A Era Mesozoica (Triássico, Jurássico e Cretáceo)."),
            QuizQuestion("Qual destes era herbívoro?", listOf("T. rex", "Brachiosaurus", "Spinosaurus"), 1, "Brachiosaurus era um saurópode herbívoro.")
        ),
        "Médio" to listOf(
            QuizQuestion("Qual era a dieta do Espinossauro?", listOf("Herbívoro", "Carnívoro", "Piscívoro"), 2, "Piscívoro."),
            QuizQuestion("Qual dinossauro tinha uma vela neural e viveu no Norte da África?", listOf("Spinosaurus", "Allosaurus", "Iguanodon"), 0, "Spinosaurus aegyptiacus."),
            QuizQuestion("Qual é considerado um elo entre dinossauros e aves?", listOf("Archaeopteryx", "Diplodocus", "Ankylosaurus"), 0, "Archaeopteryx, do Jurássico Superior."),
            QuizQuestion("Qual dinossauro tinha uma clava na cauda?", listOf("Ankylosaurus", "Stegosaurus", "Carnotaurus"), 0, "Ankylosaurus."),
            QuizQuestion("Em que período viveu o Stegosaurus?", listOf("Triássico", "Jurássico", "Cretáceo"), 1, "Jurássico Superior.")
        ),
        "Difícil" to listOf(
            QuizQuestion("Quem sobreviveu ao K-Pg?", listOf("Todos", "Aves", "Pterossauros"), 1, "Aves, dinossauros terópodes."),
            QuizQuestion("Qual cratera é associada à extinção K-Pg?", listOf("Chicxulub", "Vredefort", "Sudbury"), 0, "Chicxulub, na península de Yucatán."),
            QuizQuestion("Qual evento vulcânico coincidiu com o fim do Cretáceo?", listOf("Trapps do Decão", "Yellowstone", "Krakatoa"), 0, "Os Trapps do Decão, na Índia."),
            QuizQuestion("Em que período a Pangeia começou a se fragmentar?", listOf("Triássico", "Jurássico", "Permiano"), 1, "Jurássico, conforme os dados do app."),
            QuizQuestion("Qual grupo inclui Tyrannosaurus e Velociraptor?", listOf("Terópodes", "Saurópodes", "Ceratopsídeos"), 0, "Ambos são terópodes.")
        )
    )
}

object ArvoreInfo {
    val DESCRICOES: Map<String, String> = mapOf(
        "Reptilia" to "Grande grupo de vertebrados amniotas: lagartos, cobras, tartarugas, crocodilos, dinossauros e, cladisticamente, as aves.",
        "Archosauria" to "Reúne crocodilomorfos, pterossauros, dinossauros e aves.",
        "Dinosauria" to "Surgiram no Triássico. Dividem-se em Saurischia e Ornithischia; as aves são dinossauros terópodes vivos.",
        "Pterosauria" to "Répteis voadores do Mesozoico, parentes próximos dos dinossauros, mas não dinossauros.",
        "Saurischia" to "Dinossauros de \"quadril de lagarto\": terópodes e sauropodomorfos.",
        "Ornithischia" to "Dinossauros de \"quadril de ave\", predominantemente herbívoros: ceratopsianos, ornitópodes, estegossauros e anquilossauros.",
        "Theropoda" to "Dinossauros bípedes, em sua maioria carnívoros; deram origem às aves.",
        "Sauropodomorpha" to "Herbívoros de pescoço longo, incluindo os maiores animais terrestres.",
        "Tyrannosauridae" to "Grandes predadores do Cretáceo Superior, como o Tyrannosaurus rex.",
        "Dromaeosauridae" to "Terópodes com garra em foice no pé, como Velociraptor e Deinonychus.",
        "Spinosauridae" to "Terópodes de focinho alongado ligados a ambientes aquáticos, como o Spinosaurus.",
        "Brachiosauridae" to "Saurópodes de patas dianteiras longas e pescoço erguido, como o Brachiosaurus.",
        "Diplodocidae" to "Saurópodes de pescoço e cauda muito longos, como Diplodocus e Apatosaurus.",
        "Ceratopsia" to "Herbívoros com bico e, em muitos casos, chifres e folho ósseo, como o Triceratops.",
        "Ornithopoda" to "Herbívoros como Iguanodon e os hadrossauros (ex.: Parasaurolophus).",
        "Stegosauria" to "Herbívoros com placas e espinhos dorsais e na cauda, como o Stegosaurus.",
        "Ankylosauria" to "Herbívoros com armadura óssea, alguns com clava na cauda, como o Ankylosaurus."
    )
}
