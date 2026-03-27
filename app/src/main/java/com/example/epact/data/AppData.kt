package com.example.epact.data

import com.example.epact.model.Company
import com.example.epact.model.Metric
import com.example.epact.R

object AppData {

    val metrics = listOf(
        Metric("Empresas", "+50", "business"),
        Metric("Postos de trabalho", "+500", "groups"),
        Metric("Edifícios", "3", "apartment"),
        Metric("Projetos liderados", "5", "trend")
    )

    val companies = listOf(
        Company(
            id = 1,
            name = "TE Connectivity",
            category = "Tecnologia",
            city = "",
            shortDescription = "Empresa global de conectividade e soluções tecnológicas.",
            fullDescription = "A TE Connectivity é uma empresa internacional focada em conectividade e soluções eletrónicas aplicadas a vários setores industriais e tecnológicos.",
            website = "https://www.te.com/pt/home.html",
            tags = listOf("Conectividade", "Tecnologia", "Indústria"),
            logoRes = R.drawable.te
        ),
        Company(
            id = 2,
            name = "KPMG",
            category = "Consultoria",
            city = "Suiça",
            shortDescription = "Empresa global de auditoria, consultoria e serviços profissionais.",
            fullDescription = "A KPMG é uma referência internacional em auditoria, consultoria, fiscalidade e apoio estratégico a organizações de diferentes setores.",
            website = "https://kpmg.com/pt/pt.html",
            tags = listOf("Consultoria", "Auditoria", "Estratégia"),
            logoRes = R.drawable.kpmg
        ),
        Company(
            id = 3,
            name = "CEiiA",
            category = "Inovação",
            city = "",
            shortDescription = "Centro de engenharia e desenvolvimento com foco em inovação e mobilidade.",
            fullDescription = "O CEiiA trabalha em engenharia, inovação, mobilidade e desenvolvimento tecnológico, criando soluções com impacto nacional e internacional.",
            website = "https://www.ceiia.com/",
            tags = listOf("Engenharia", "Inovação", "Mobilidade"),
            logoRes = R.drawable.ceiia
        ),
        Company(
            id = 4,
            name = "Jerónimo Martins",
            category = "Retalho",
            city = "",
            shortDescription = "Grupo empresarial com forte presença no setor da distribuição e retalho.",
            fullDescription = "A Jerónimo Martins é um grupo empresarial de referência ligado à distribuição alimentar e retalho, com forte presença em vários mercados.",
            website = "https://www.jeronimomartins.com/pt/",
            tags = listOf("Retalho", "Distribuição", "Grupo"),
            logoRes = R.drawable.jeronimomartins
        ),
        Company(
            id = 5,
            name = "Empowered Startups",
            category = "Empreendedorismo",
            city = "",
            shortDescription = "Organização focada em apoio a startups e crescimento empresarial.",
            fullDescription = "A Empowered Startups desenvolve programas e iniciativas ligadas ao apoio a startups, inovação e aceleração de negócios.",
            website = "https://empoweredstartups.com/",
            tags = listOf("Startups", "Empreendedorismo", "Inovação"),
            logoRes = R.drawable.empowered
        ),
        Company(
            id = 6,
            name = "ITGest",
            category = "Tecnologia",
            city = "",
            shortDescription = "Empresa de soluções tecnológicas e transformação digital.",
            fullDescription = "A ITGest atua na área das tecnologias de informação, desenvolvimento de soluções empresariais e transformação digital.",
            website = "https://www.itgest.pt/",
            tags = listOf("Software", "Transformação Digital", "Tecnologia"),
            logoRes = R.drawable.itgest
        ),
        Company(
            id = 7,
            name = "N10GLED",
            category = "Tecnologia",
            city = "",
            shortDescription = "Projeto ligado a soluções tecnológicas e inovação digital.",
            fullDescription = "A N10GLED integra o ecossistema com foco em inovação tecnológica e desenvolvimento de soluções digitais.",
            website = "https://www.entangled-space.com/",
            tags = listOf("Tecnologia", "Inovação", "Digital"),
            logoRes = R.drawable.n10gled
        ),
        Company(
            id = 8,
            name = "NTT DATA",
            category = "Tecnologia",
            city = "",
            shortDescription = "Multinacional de consultoria tecnológica e serviços digitais.",
            fullDescription = "A NTT DATA é uma empresa global especializada em tecnologia, consultoria, dados, cloud e transformação digital.",
            website = "https://www.nttdata.com/global/en/",
            tags = listOf("Consultoria", "Cloud", "Dados"),
            logoRes = R.drawable.ntt
        ),
        Company(
            id = 9,
            name = "Fraunhofer Portugal",
            category = "Investigação",
            city = "",
            shortDescription = "Centro de investigação aplicada e desenvolvimento tecnológico.",
            fullDescription = "A Fraunhofer Portugal trabalha em investigação aplicada, desenvolvimento experimental e criação de soluções tecnológicas com impacto real.",
            website = "https://www.fraunhofer.pt/",
            tags = listOf("Investigação", "Tecnologia", "Desenvolvimento"),
            logoRes = R.drawable.fhp
        ),
        Company(
            id = 10,
            name = "IG&H",
            category = "Consultoria",
            city = "",
            shortDescription = "Empresa de consultoria e transformação orientada para inovação.",
            fullDescription = "A IG&H desenvolve soluções e serviços de consultoria focados em inovação, estratégia e transformação organizacional.",
            website = "https://www.igh.com/",
            tags = listOf("Consultoria", "Transformação", "Inovação"),
            logoRes = R.drawable.igh
        ),
        Company(
            id = 11,
            name = "DigitalWorks",
            category = "Tecnologia",
            city = "",
            shortDescription = "Empresa ligada a engenharia, criatividade e soluções digitais.",
            fullDescription = "A DigitalWorks atua na área da tecnologia e engenharia, combinando criatividade com desenvolvimento de soluções digitais.",
            website = "https://www.plexus.es/",
            tags = listOf("Tecnologia", "Engenharia", "Digital"),
            logoRes = R.drawable.digitalworks
        ),
        Company(
            id = 12,
            name = "IPParking",
            category = "Mobilidade",
            city = "",
            shortDescription = "Soluções tecnológicas para estacionamento e mobilidade.",
            fullDescription = "A IPParking desenvolve sistemas inteligentes ligados ao estacionamento, controlo de acessos e mobilidade urbana.",
            website = "https://www.ipparking.com/en/",
            tags = listOf("Mobilidade", "Estacionamento", "Smart City"),
            logoRes = R.drawable.ipparking
        ),
        Company(
            id = 13,
            name = "SDAC",
            category = "Tecnologia",
            city = "",
            shortDescription = "Soluções digitais de apoio às comunidades.",
            fullDescription = "A SDAC desenvolve soluções digitais pensadas para responder a necessidades comunitárias, sociais e de proximidade.",
            website = "https://sdac.pt/",
            tags = listOf("Digital", "Comunidades", "Inovação"),
            logoRes = R.drawable.sdac
        ),
        Company(
            id = 14,
            name = "Peak&Peak",
            category = "Tecnologia",
            city = "",
            shortDescription = "Empresa orientada para tecnologia, dados e inovação digital.",
            fullDescription = "A Peak&Peak integra soluções tecnológicas e digitais, com foco em inovação, desempenho e desenvolvimento de produtos e serviços.",
            website = "https://ppeak.com/",
            tags = listOf("Tecnologia", "Dados", "Inovação"),
            logoRes = R.drawable.peak
        ),
        Company(
            id = 15,
            name = "Vidigal Silva & Carlos Silva",
            category = "Consultoria",
            city = "",
            shortDescription = "Empresa de contabilidade e consultoria fiscal.",
            fullDescription = "A Vidigal Silva & Carlos Silva presta serviços de contabilidade, consultoria e apoio fiscal a empresas e organizações.",
            website = "https://www.vcsilva.pt/",
            tags = listOf("Contabilidade", "Fiscalidade", "Consultoria"),
            logoRes = R.drawable.vsc
        ),
        Company(
            id = 16,
            name = "foursolutions",
            category = "Tecnologia",
            city = "",
            shortDescription = "Empresa de soluções tecnológicas e digitais.",
            fullDescription = "A foursolutions trabalha em desenvolvimento de soluções tecnológicas com foco em digitalização, eficiência e inovação.",
            website = "https://foursolutions.pt/",
            tags = listOf("Tecnologia", "Software", "Soluções"),
            logoRes = R.drawable.foursolutions
        ),
        Company(
            id = 17,
            name = "Qstaff",
            category = "Recursos Humanos",
            city = "",
            shortDescription = "Empresa ligada a recrutamento e gestão de talento.",
            fullDescription = "A Qstaff atua na área dos recursos humanos, recrutamento e apoio à gestão de equipas e talento.",
            website = "https://www.qstaff.pt/",
            tags = listOf("RH", "Recrutamento", "Talento"),
            logoRes = R.drawable.q_staff
        ),
        Company(
            id = 18,
            name = "Interprev",
            category = "Segurança",
            city = "",
            shortDescription = "Empresa focada em prevenção, segurança e saúde no trabalho.",
            fullDescription = "A Interprev presta serviços ligados à segurança, prevenção de riscos e saúde ocupacional em contexto empresarial.",
            website = "https://www.interprev.pt/",
            tags = listOf("Segurança", "Prevenção", "Saúde"),
            logoRes = R.drawable.interprev
        ),
        Company(
            id = 19,
            name = "BSO Consulting",
            category = "Consultoria",
            city = "",
            shortDescription = "Empresa de consultoria empresarial e organizacional.",
            fullDescription = "A BSO Consulting trabalha em consultoria e apoio estratégico a empresas, com foco em crescimento e melhoria organizacional.",
            website = "https://www.linkedin.com/company/bsoconsulting/",
            tags = listOf("Consultoria", "Gestão", "Estratégia"),
            logoRes = R.drawable.bso
        ),
        Company(
            id = 20,
            name = "PropWorx",
            category = "Tecnologia",
            city = "",
            shortDescription = "Soluções tecnológicas ligadas a ativos, propriedade e inovação digital.",
            fullDescription = "A PropWorx desenvolve soluções orientadas para tecnologia, inovação e apoio digital em áreas ligadas a gestão e ativos.",
            website = "https://propworx.pt/",
            tags = listOf("Tecnologia", "Digital", "Gestão"),
            logoRes = R.drawable.propworx
        ),
        Company(
            id = 21,
            name = "Solvit",
            category = "Tecnologia",
            city = "",
            shortDescription = "Empresa de inovação em telecomunicações e tecnologia.",
            fullDescription = "A Solvit trabalha em soluções de telecomunicações, inovação técnica e desenvolvimento de serviços digitais.",
            website = "https://solvit.pt/",
            tags = listOf("Telecomunicações", "Tecnologia", "Inovação"),
            logoRes = R.drawable.solvit
        ),
        Company(
            id = 22,
            name = "Caja Rural",
            category = "Financeiro",
            city = "",
            shortDescription = "Entidade ligada ao setor financeiro e cooperativo.",
            fullDescription = "A Caixa Rural representa uma presença do setor financeiro e cooperativo, com foco em apoio económico e serviços financeiros.",
            website = "https://www.grupocajarural.es/es",
            tags = listOf("Financeiro", "Cooperativo", "Serviços"),
            logoRes = R.drawable.cajarural
        ),
        Company(
            id = 23,
            name = "Verde100Truques",
            category = "Serviços",
            city = "Évora",
            shortDescription = "Empresa ligada a serviços e atividade local.",
            fullDescription = "A Verde100Truques integra o ecossistema como entidade empresarial ligada a serviços e presença local.",
            website = "https://www.northdata.com/Verde100Truques%20Lda%C2%B7,%20%C3%89vora/PT%20518601951",
            tags = listOf("Serviços", "Local", "Empresa"),
            logoRes = R.drawable.verdesemtruques
        ),
        Company(
            id = 24,
            name = "Galhetas Catering",
            category = "Restauração",
            city = "",
            shortDescription = "Empresa de catering e serviços de restauração.",
            fullDescription = "A Galhetas Catering presta serviços de catering, apoio alimentar e soluções de restauração para diferentes contextos.",
            website = "https://www.galhetas.com/",
            tags = listOf("Catering", "Restauração", "Serviços"),
            logoRes = R.drawable.galhetas
        )
    )

    val categories = listOf(
        "Todos",
        "Tecnologia",
        "Consultoria",
        "Inovação",
        "Investigação",
        "Retalho",
        "Empreendedorismo",
        "Mobilidade",
        "Recursos Humanos",
        "Segurança",
        "Financeiro",
        "Serviços",
        "Restauração"
    )

    const val pactAddress = "Herdade da Barba Rala, R. Luís Adelino Fonseca 1A, 7005-345 Évora"
}