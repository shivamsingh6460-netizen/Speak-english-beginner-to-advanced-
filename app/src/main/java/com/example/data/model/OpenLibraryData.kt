package com.example.data.model

object OpenLibraryData {

    val books: List<OpenBook> = listOf(
        OpenBook(
            id = "book_1",
            title = "The Gift of the Magi",
            titleHindi = "द गिफ्ट ऑफ द मेजाई (सच्चा प्रेम और त्याग)",
            author = "O. Henry",
            authorHindi = "ओ. हेनरी",
            year = "1905",
            coverEmoji = "🎁",
            category = "Short Stories",
            difficultyLevel = "Beginner (आसान)",
            estimatedReadTimeMinutes = 12,
            descriptionEn = "A heartwarming classic story about a young husband and wife who give up their most precious possessions to buy Christmas gifts for each other.",
            descriptionHindi = "एक दिल छू लेने वाली अमर कहानी, जहाँ एक गरीब पति-पत्नी एक दूसरे को अनमोल उपहार देने के लिए अपनी सबसे प्यारी चीजें त्याग देते हैं।",
            gutenbergOrOpenLibraryUrl = "https://www.gutenberg.org/ebooks/7256",
            tags = listOf("Love", "Sacrifice", "Classic", "Emotional"),
            chapters = listOf(
                BookChapter(
                    chapterNumber = 1,
                    title = "Chapter 1: One Dollar and Eighty-Seven Cents",
                    titleHindi = "अध्याय 1: एक डॉलर और सतासी सेंट",
                    summaryHindi = "डेला के पास जिम के लिए क्रिसमस का तोहफा खरीदने के लिए सिर्फ $1.87 बचे हैं। वह रोने लगती है और फिर एक अनोखा फैसला करती है।",
                    content = """One dollar and eighty-seven cents. That was all. And sixty cents of it was in pennies. Pennies saved one and two at a time by bulldozing the grocer and the vegetable man and the butcher until one's cheeks burned with the silent imputation of parsimony that such close dealing implied.

Three times Della counted it. One dollar and eighty-seven cents. And the next day would be Christmas.

There was clearly nothing to do but flop down on the shabby little couch and howl. So Della did it. Which instigates the moral reflection that life is made up of sobs, sniffles, and smiles, with sniffles predominating.

While the mistress of the home is gradually subsiding from the first stage to the second, take a look at the home. A furnished flat at $8 per week. It did not exactly beggar description, but it certainly had that word on the lookout for the mendicancy squad.

In the vestibule below was a letter-box into which no letter would go, and an electric button from which no mortal finger could coax a ring. Also appertaining thereunto was a card bearing the name 'Mr. James Dillingham Young.'

Della finished her cry and attended to her cheeks with the powder rag. She stood by the window and looked out dully at a gray cat walking a gray fence in a gray backyard. Tomorrow would be Christmas Day, and she had only $1.87 with which to buy Jim a present.

Now, there were two possessions of the James Dillingham Youngs in which they both took a mighty pride. One was Jim's gold watch that had been his father's and his grandfather's. The other was Della's hair.

Della's beautiful hair fell about her, rippling and shining like a cascade of brown waters. It reached below her knee and made itself almost a garment for her. And then she did it up again nervously and quickly. Once she faltered for a minute and stood still while a tear or two splashed on the worn red carpet.

On went her old brown jacket; on went her old brown hat. With a whirl of skirts and with the brilliant sparkle still in her eyes, she fluttered out the door and down the stairs to the street.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Parsimony", "अत्यधिक कंजूसी", "पार्सिमोनी", "Her parsimony helped her save money."),
                        BookWordGlossary("Possessions", "संपत्ति / धरोहर", "पज़ेशन्स", "Their most valuable possessions were a watch and hair."),
                        BookWordGlossary("Cascade", "झरने की तरह गिरना", "कैस्केड", "Her hair fell in a shining cascade."),
                        BookWordGlossary("Faltered", "हिचकिचाना / लड़खड़ाना", "फॉल्टर्ड", "She faltered for a second before making up her mind.")
                    )
                ),
                BookChapter(
                    chapterNumber = 2,
                    title = "Chapter 2: The Beautiful Sacrifice & The Platinum Chain",
                    titleHindi = "अध्याय 2: सुंदर त्याग और प्लैटिनम की चेन",
                    summaryHindi = "डेला अपने बाल बेचकर जिम की अनमोल घड़ी के लिए एक शानदार प्लैटिनम चेन खरीदती है।",
                    content = """Where she stopped the sign read: 'Mme. Sofronie. Hair Goods of All Kinds.' One flight up Della ran, and collected herself, panting.

'Will you buy my hair?' asked Della.

'I buy hair,' said Madame. 'Take yer hat off and let's have a sight at the looks of it.'

Down rippled the brown cascade.

'Twenty dollars,' said Madame, lifting the mass with a practised hand.

'Give it to me quick,' said Della.

Oh, and the next two hours tripped by on rosy wings. Forget the hashed metaphor. She was ransacking the stores for Jim's present.

She found it at last. It surely had been made for Jim and no one else. There was no other like it in any of the stores, and she had turned all of them inside out. It was a platinum fob chain, simple and chaste in design, properly proclaiming its value by substance alone and not by meretricious ornamentation—as all good things should do. It was even worthy of The Watch.

As soon as she saw it she knew that it must be Jim's. It was like him. Quietness and value—the description applied to both. Twenty-one dollars they took from her for it, and she hurried home with the 87 cents.

When Della reached home her intoxication gave way a little to prudence and reason. She got out her curling irons and lighted the gas and went to work repairing the ravages made by generosity added to love. Which is always a tremendous task, dear friends—a mammoth task.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Ransacking", "ढूँढने के लिए खंगालना", "रैनसैकिंग", "She spent hours ransacking the shops."),
                        BookWordGlossary("Chaste", "सादा और पवित्र", "चेस्ट", "A chaste and elegant platinum design."),
                        BookWordGlossary("Prudence", "समझदारी / विवेक", "प्रूडेंस", "Prudence guided her next decision.")
                    )
                ),
                BookChapter(
                    chapterNumber = 3,
                    title = "Chapter 3: The Magis' True Wisdom",
                    titleHindi = "अध्याय 3: बुद्धिमान मेजाई और सच्चा उपहार",
                    summaryHindi = "जिम घर आता है और देखता है कि उसने डेला के बालों के लिए सुंदर कंघी खरीदने हेतु अपनी घड़ी बेच दी थी। दोनों का प्रेम सबसे बड़ा उपहार साबित होता है।",
                    content = """Jim stopped inside the door, as immovable as a setter at the scent of quail. His eyes were fixed upon Della, and there was an expression in them that she could not read, and it terrified her. It was not anger, nor surprise, nor disapproval, nor horror, nor any of the sentiments that she had been prepared for. He simply stared at her with that peculiar expression on his face.

Della wriggled off the table and went for him.

'Jim, darling,' she cried, 'don't look at me that way. I had my hair cut off and sold because I couldn't have lived through Christmas without giving you a present. It'll grow out again—you won't mind, will you? My hair grows awfully fast. Say 'Merry Christmas!' Jim, and let's be happy. You don't know what a nice—what a beautiful, nice gift I've got for you.'

'You've cut off your hair?' asked Jim, laboriously, as if he had not arrived at that patent fact yet even after the hardest mental labor.

'Cut it off and sold it,' said Della. 'Don't you like me just as well, anyhow? I'm me without my hair, ain't I?'

Jim drew a package from his overcoat pocket and threw it upon the table.

White fingers tore at the string and paper. And then an ecstatic scream of joy; and then, alas! a quick feminine change to hysterical tears and wails.

For there lay The Combs—the set of combs, side and back, that Della had worshipped for long in a Broadway window. Beautiful combs, pure tortoise shell, with jewelled rims—just the shade to wear in the beautiful vanished hair.

'My hair grows so fast, Jim!'

And then Della leaped up like a little singed cat and cried, 'Oh, oh!'

Jim had not yet seen his beautiful present. She held it out to him eagerly upon her open palm. The dull precious metal seemed to flash with a reflection of her bright and ardent spirit.

'Isn't it a dandy, Jim? I hunted all over town to find it. You'll have to look at the time a hundred times a day now. Give me your watch. I want to see how it looks on it.'

Instead of obeying, Jim tumbled down on the couch and put his hands under the back of his head and smiled.

'Dell,' said he, 'let's put our Christmas presents away and keep 'em a while. They're too nice to use just at present. I sold the watch to get the money to buy your combs.'

The magi, as you know, were wise men—wonderfully wise men—who brought gifts to the Babe in the manger. They invented the art of giving Christmas presents. Being wise, their gifts were no doubt wise ones. And here I have lamely related to you the uneventful chronicle of two foolish children in a flat who most unwisely sacrificed for each other the greatest treasures of their house.

But in a last word to the wise of these days let it be said that of all who give gifts these two were the wisest. Of all who give and receive gifts, such as they are wisest. Everywhere they are wisest. They are the magi.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Ardent", "उत्साही / प्रबल", "आर्डेंट", "Her ardent spirit filled the room with joy."),
                        BookWordGlossary("Chronicle", "इतिहास / कथा", "क्रॉनिकल", "A touching chronicle of true devotion."),
                        BookWordGlossary("Magi", "बुद्धिमान दानी", "मेजाई", "The wisest givers who understand true love.")
                    )
                )
            )
        ),
        OpenBook(
            id = "book_2",
            title = "The Adventures of Sherlock Holmes: A Scandal in Bohemia",
            titleHindi = "शेरलॉक होम्स: बोहेमिया का एक रहस्य",
            author = "Arthur Conan Doyle",
            authorHindi = "आर्थर कॉनन डॉयल",
            year = "1891",
            coverEmoji = "🕵️‍♂️",
            category = "Adventure",
            difficultyLevel = "Intermediate (मध्यम)",
            estimatedReadTimeMinutes = 18,
            descriptionEn = "Sherlock Holmes is hired by the King of Bohemia to recover an indiscreet photograph, only to meet the clever and formidable Irene Adler.",
            descriptionHindi = "बोहेमिया के राजा शेरलॉक होम्स से एक गुप्त तस्वीर वापस पाने में मदद मांगते हैं, जहाँ होम्स का सामना चालाक आइरीन एडलर से होता है।",
            gutenbergOrOpenLibraryUrl = "https://www.gutenberg.org/ebooks/1661",
            tags = listOf("Mystery", "Detective", "Classic", "London"),
            chapters = listOf(
                BookChapter(
                    chapterNumber = 1,
                    title = "Chapter 1: The Observation of Sherlock Holmes",
                    titleHindi = "अध्याय 1: शेरलॉक होम्स की तीव्र अवलोकन शक्ति",
                    summaryHindi = "डॉ. वॉटसन 221B बेकर स्ट्रीट पर होम्स से मिलते हैं और होम्स एक रहस्यमयी पत्र और आगंतुक का सटीक विश्लेषण करते हैं।",
                    content = """To Sherlock Holmes she is always THE woman. I have seldom heard him mention her under any other name. In his eyes she eclipses and predominates the whole of her sex.

One night—it was on the twentieth of March, 1888—I was returning from a journey to a patient, for I had now returned to civil practice, when my way led me through Baker Street. As I passed the well-remembered door, which must always be associated in my mind with my wooing, and with the dark incidents of the Study in Scarlet, I was seized with a keen desire to see Holmes again, and to know how he was employing his extraordinary powers.

His rooms were brilliantly lit, and, even as I looked up, I saw his tall, spare figure pass twice in a dark silhouette against the blind. He was pacing the room swiftly, eagerly, with his head sunk upon his chest and his hands clasped behind him.

'You see, Watson,' said he, 'you see, but you do not observe. The distinction is clear. For example, you have frequently seen the steps which lead up from the hall to this room.'

'Frequently.'

'How often?'

'Well, some hundreds of times.'

'Then how many are there?'

'How many? I don't know.'

'Quite so! You have not observed. And yet you have seen. That is just my point. Now, I know that there are seventeen steps, because I have both seen and observed.'

He then handed me a sheet of thick, pink-tinted notepaper which had been lying open upon the table. 'It came by the last post,' said he. 'Read it aloud.'""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Eclipses", "पीछे छोड़ देना / ग्रहण लगाना", "इक्लिप्सिज़", "Her intellect eclipses everyone around her."),
                        BookWordGlossary("Observe", "बारीकी से देखना / अवलोकन करना", "ऑब्सर्व", "You see, but you do not observe."),
                        BookWordGlossary("Silhouette", "काली छाया / रूपरेखा", "सिलुएट", "His silhouette stood against the lighted window.")
                    )
                ),
                BookChapter(
                    chapterNumber = 2,
                    title = "Chapter 2: The Masked King & The Compromising Photograph",
                    titleHindi = "अध्याय 2: नकाबपोश राजा और गुप्त तस्वीर",
                    summaryHindi = "बोहेमिया के राजा गुप्त रूप से होम्स के पास आते हैं और आइरीन एडलर से अपनी तस्वीर वापस हासिल करने का आग्रह करते हैं।",
                    content = """A slow and heavy step, which had been heard upon the stairs and in the passage, paused immediately outside the door. Then there was a loud and authoritative tap.

'Come in!' said Holmes.

A man entered who could hardly have been less than six feet six inches in height, with the chest and limbs of a Hercules. His dress was rich with a richness which would, in England, be regarded as akin to bad taste. Heavy bands of astrakhan were slashed across the sleeves and fronts of his double-breasted coat, while the deep blue cloak which was thrown over his shoulders was lined with flame-coloured silk. He held a black vizard mask against the upper part of his face.

'You had my note?' he asked with a deep, harsh voice and a strongly marked German accent. 'I told you that I would call.'

'Pray take a seat,' said Holmes. 'This is my friend and colleague, Dr. Watson, who is always of assistance.'

The stranger sat down. 'You will excuse this mask,' continued our strange visitor. 'The august person who employs me wishes his agent to be unknown to you, and I may confess at once that the title by which I have just called myself is not exactly my own.'

'If your Majesty would condescend to state your case,' remarked Holmes, 'I should be better able to advise you.'

The client sprang from his chair and paced up and down the room in uncontrollable agitation. Then, with a gesture of desperation, he tore the mask from his face and hurled it upon the ground.

'You are right!' he cried; 'I am the King of Bohemia. Why should I attempt to conceal it?'""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Authoritative", "रोबदार / अधिकारपूर्ण", "ऑथॉरिटेटिव", "He knocked in a loud and authoritative manner."),
                        BookWordGlossary("Agitation", "घबराहट / बेचैनी", "एजिटेशन", "He walked the floor in great agitation."),
                        BookWordGlossary("Condescend", "कृपा करना / सहमति देना", "कॉनडिसेंड", "If you condescend to state the facts clearly.")
                    )
                )
            )
        ),
        OpenBook(
            id = "book_3",
            title = "Panchatantra: Timeless English Moral Stories",
            titleHindi = "पंचतंत्र: ज्ञानवर्धक और प्रेरक कहानियाँ",
            author = "Pandit Vishnu Sharma (Tr. Arthur W. Ryder)",
            authorHindi = "पंडित विष्णु शर्मा",
            year = "Classic",
            coverEmoji = "🦁",
            category = "Folk Tales",
            difficultyLevel = "Beginner (आसान)",
            estimatedReadTimeMinutes = 10,
            descriptionEn = "Ancient Indian animal fables delivering timeless wisdom on friendship, leadership, wisdom, and clever problem solving.",
            descriptionHindi = "मित्रता, चातुर्य और बुद्धिमानी की प्राचीन और अनमोल शिक्षाप्रद कहानियाँ जो अंग्रेजी सीखने के लिए अत्यंत सरल और रोचक हैं।",
            gutenbergOrOpenLibraryUrl = "https://openlibrary.org/works/OL1839174W/The_Panchatantra",
            tags = listOf("Wisdom", "Animals", "India", "Beginner Friendly"),
            chapters = listOf(
                BookChapter(
                    chapterNumber = 1,
                    title = "Chapter 1: The Lion and the Clever Rabbit",
                    titleHindi = "अध्याय 1: शेर और चतुर खरगोश",
                    summaryHindi = "जब क्रूर शेर भासुरक जंगल के जानवरों को सताता है, तो एक छोटा सा खरगोश अपनी बुद्धिमत्ता से शेर को कुएं में गिराकर सबकी रक्षा करता है।",
                    content = """In a certain forest lived a lion named Bhasuraka. He was exceedingly strong and haughty, and he mercilessly slaughtered the other animals of the jungle every single day.

One day, all the deer, boars, hares, and birds assembled together and approached the ferocious lion with humble respect.

'O King of the forest,' they said with folded hands, 'why do you slaughter so many innocent creatures in one day? If you kill us all, soon no animal will remain in your realm. Therefore, we propose a pact: every day, one animal will voluntarily present itself for your daily meal.'

The lion agreed, saying: 'Very well. But if ever an animal fails to arrive on time, I shall destroy every living creature in this woods!'

Days passed smoothly until it was the turn of an old and tiny rabbit. The little rabbit was small in size, but endowed with tremendous wisdom. As he walked toward the lion's den, he thought: 'A wise person uses intellect when strength fails. Why should I hurry to my demise?'

He walked as slowly as possible and arrived at the den long after sunset.

The lion was famished and beside himself with rage. 'You wretched midget!' roared the lion. 'You have kept me waiting! Why are you so late?'

The rabbit bowed meekly: 'Your Majesty, five rabbits were sent with me, but on the road, another mighty lion stopped us. He claimed that he is the true king of this forest and devoured the other four rabbits! I barely escaped to bring you this news.'

'Where is this insolent impostor?' roared the lion in fury. 'Lead me to him at once!'

The rabbit led the lion to a deep, dark well with crystal-clear water. 'He hides in this fortress, Sire,' whispered the rabbit.

The lion peered down into the well and saw his own reflection staring back with fierce eyes. Believing it to be his rival, the foolish lion let out a thunderous roar. The echo bounced back with double intensity. In blind fury, the lion sprang down into the well to attack his enemy and drowned instantly.

The clever rabbit rejoiced and returned to tell the good news. All the animals lived in peace ever after.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Haughty", "अहंकारी / घमंडी", "हॉटी", "A haughty and arrogant leader."),
                        BookWordGlossary("Demise", "मृत्यु / अंत", "डिमाइज़", "He planned carefully to avoid his demise."),
                        BookWordGlossary("Famished", "अत्यधिक भूखा", "फैमिश्ड", "After a long trek, he was completely famished."),
                        BookWordGlossary("Insolent", "गुस्ताख / ढीठ", "इनसोलेंट", "The insolent behavior annoyed everyone.")
                    )
                ),
                BookChapter(
                    chapterNumber = 2,
                    title = "Chapter 2: The Four Friendly Companions",
                    titleHindi = "अध्याय 2: चार सच्चे मित्र (एकता में शक्ति)",
                    summaryHindi = "एक चूहा, कौआ, कछुआ और हिरण कैसे अपनी गहरी दोस्ती और तालमेल से शिकारी के जाल से एक दूसरे को बचाते हैं।",
                    content = """In a lush valley near a shimmering lake, four unlikely creatures lived as inseparable friends: Laghupatanaka the crow, Mantharaka the tortoise, Hiranyaka the mouse, and Chitranga the spotted deer.

Every afternoon, they would gather beneath the shade of a banyan tree to share stories and words of wisdom.

One day, while grazing, the deer Chitranga was ensnared in a hunter's strong net made of thick cords. When the deer did not return at dusk, the crow flew high into the sky and located his trapped companion.

'Do not despair, dear brother!' cried the crow. 'True friends prove their worth in times of peril.'

The crow immediately flew back, lifted the mouse Hiranyaka on his back, and brought him directly to the trapped deer. With his sharp teeth, the mouse swiftly began gnawing the tough cords.

Just as the last knot was severed, the hunter approached with bow and arrows.

The crow flew into the highest branches; the mouse darted into a tiny hole in the ground; the swift deer bounded away into the thicket. Only the slow tortoise remained on the path. The frustrated hunter seized the tortoise, tied him in a sack, and flung it over his shoulder.

The three friends immediately devised a cunning plan. The deer ran ahead and lay motionlessly near the pond, pretending to be dead, while the crow perched on his antler pretending to peck at his eyes.

Seeing what appeared to be a fresh carcass, the greedy hunter dropped the sack containing the tortoise and rushed toward the deer. The moment he came near, the deer sprang up and vanished into the woods. Meanwhile, the mouse raced to the sack and chewed through the cords, freeing the tortoise, who slipped safely into the lake water.

United in wisdom and loyalty, the four friends reunited safely under their tree, proving that mutual trust and cooperation conquer all difficulties.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Inseparable", "अटूट / जिन्हें अलग न किया जा सके", "इनसेपरेबल", "They were inseparable childhood companions."),
                        BookWordGlossary("Peril", "विपत्ति / गंभीर संकट", "पेरिल", "A true companion stands by you in times of peril."),
                        BookWordGlossary("Gnawing", "दांतों से कुतरना", "नॉइंग", "The mouse was busy gnawing the cords.")
                    )
                )
            )
        ),
        OpenBook(
            id = "book_4",
            title = "Steve Jobs: 2005 Stanford Commencement Speech",
            titleHindi = "स्टीव जॉब्स: 2005 स्टैनफोर्ड दीक्षांत भाषण",
            author = "Steve Jobs",
            authorHindi = "स्टीव जॉब्स (Apple सह-संस्थापक)",
            year = "2005",
            coverEmoji = "🎓",
            category = "Inspiration",
            difficultyLevel = "Beginner (आसान)",
            estimatedReadTimeMinutes = 8,
            descriptionEn = "One of the most famous inspirational speeches in modern history about connecting the dots, love and loss, and staying hungry & foolish.",
            descriptionHindi = "इतिहास का सबसे प्रेरक भाषण: 'डॉट्स को जोड़ना', 'प्रेम और असफलता', और 'हमेशा भूखे रहो, मूर्ख बने रहो' (Stay Hungry, Stay Foolish)।",
            gutenbergOrOpenLibraryUrl = "https://news.stanford.edu/stories/2005/06/youve-got-find-what-you-love-jobs-says-2005",
            tags = listOf("Motivation", "Career", "Life Lessons", "English Speeches"),
            chapters = listOf(
                BookChapter(
                    chapterNumber = 1,
                    title = "Chapter 1: Connecting the Dots",
                    titleHindi = "अध्याय 1: जीवन की घटनाओं (डॉट्स) को जोड़ना",
                    summaryHindi = "स्टीव जॉब्स बताते हैं कि कॉलेज छोड़ने के बाद सीखी गई सुलेखन (Calligraphy) कला ने आगे चलकर Mac कंप्यूटर को दुनिया का पहला सुंदर टाइपोग्राफी वाला कंप्यूटर बनाया।",
                    content = """I am honored to be with you today at your commencement from one of the finest universities in the world. I never graduated from college. Truth be told, this is the closest I've ever gotten to a college graduation. Today I want to tell you three stories from my life. That's it. No big deal. Just three stories.

The first story is about connecting the dots.

I dropped out of Reed College after the first 6 months, but then stayed around as a drop-in for another 18 months or so before I really quit. So why did I drop out?

It started before I was born. My biological mother was a young, unwed college graduate student, and she decided to put me up for adoption.

Reed College at that time offered perhaps the best calligraphy instruction in the country. Throughout the campus every poster, every label on every drawer, was beautifully hand calligraphed. Because I had dropped out and didn't have to take the normal classes, I decided to take a calligraphy class to learn how to do this. I learned about serif and sans serif typefaces, about varying the amount of space between different letter combinations, about what makes great typography great. It was beautiful, historical, artistically subtle in a way that science can't capture, and I found it fascinating.

None of this had even a hope of any practical application in my life. But 10 years later, when we were designing the first Macintosh computer, it all came back to me. And we designed it all into the Mac. It was the first computer with beautiful typography. If I had never dropped in on that single course in college, the Mac would have never had multiple typefaces or proportionally spaced fonts.

You can't connect the dots looking forward; you can only connect them looking backward. So you have to trust that the dots will somehow connect in your future. You have to trust in something — your gut, destiny, life, karma, whatever. This approach has never let me down, and it has made all the difference in my life.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Commencement", "दीक्षांत समारोह / नई शुरुआत", "कमेंसमेंट", "A memorable university commencement speech."),
                        BookWordGlossary("Typography", "मुद्रण कला / अक्षरों की बनावट", "टाइपोग्राफी", "Beautiful typography elevates reading experience."),
                        BookWordGlossary("Subtle", "सूक्ष्म / कोमल", "सटल", "There was a subtle elegance in the handcrafted design.")
                    )
                ),
                BookChapter(
                    chapterNumber = 2,
                    title = "Chapter 2: Love, Loss & Stay Hungry, Stay Foolish",
                    titleHindi = "अध्याय 2: प्रेम, असफलता और सदा जिज्ञासु रहना",
                    summaryHindi = "30 साल की उम्र में अपनी ही कंपनी Apple से निकाले जाने के बाद उन्होंने NeXT और Pixar की शुरुआत की और जीवन में कभी हार न मानने की सीख दी।",
                    content = """My second story is about love and loss.

I was lucky — I found what I loved to do early in life. Woz and I started Apple in my parents' garage when I was 20. We worked hard, and in 10 years Apple had grown from just the two of us in a garage into a ${'$'}2 billion company with over 4,000 employees. We had just released our finest creation — the Macintosh — a year earlier, and I had just turned 30. And then I got fired.

How can you get fired from a company you started? Well, as Apple grew we hired someone who I thought was very talented to run the company with me, and for the first year or so things went well. But then our visions of the future began to diverge and eventually we had a falling out. When we did, our Board of Directors sided with him. So at 30 I was out. And very publicly out.

I didn't see it then, but it turned out that getting fired from Apple was the best thing that could have ever happened to me. The heaviness of being successful was replaced by the lightness of being a beginner again, less sure about everything. It freed me to enter one of the most creative periods of my life.

During the next five years, I started a company named NeXT, another company named Pixar, and fell in love with an amazing woman who would become my wife. Pixar went on to create the world's first computer animated feature film, Toy Story, and is now the most successful animation studio in the world. In a remarkable turn of events, Apple bought NeXT, I returned to Apple, and the technology we developed at NeXT is at the heart of Apple's current renaissance.

Your work is going to fill a large part of your life, and the only way to be truly satisfied is to do what you believe is great work. And the only way to do great work is to love what you do. If you haven't found it yet, keep looking. Don't settle.

When I was young, there was an amazing publication called The Whole Earth Catalog, which was one of the bibles of my generation. On the back cover of their final issue was a photograph of an early morning country road, the kind you might find yourself hitchhiking on if you were so adventurous. Beneath it were the words: 'Stay Hungry. Stay Foolish.'

It was their farewell message as they signed off. Stay Hungry. Stay Foolish. And I have always wished that for myself. And now, as you graduate to begin anew, I wish that for you.

Stay Hungry. Stay Foolish.""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Diverge", "अलग-अलग दिशाओं में जाना", "डाइवर्ज", "Their long-term plans began to diverge."),
                        BookWordGlossary("Renaissance", "पुनर्जागरण / नया स्वर्णिम काल", "रेनेसांस", "The company entered a grand technological renaissance."),
                        BookWordGlossary("Farewell", "विदाई / शुभकामना", "फेयरवेल", "A memorable farewell note for the graduates.")
                    )
                )
            )
        ),
        OpenBook(
            id = "book_5",
            title = "The Time Machine",
            titleHindi = "द टाइम मशीन (काल-यात्रा का रोमांच)",
            author = "H.G. Wells",
            authorHindi = "एच.जी. वेल्स",
            year = "1895",
            coverEmoji = "⏳",
            category = "Sci-Fi",
            difficultyLevel = "Advanced (उन्नत)",
            estimatedReadTimeMinutes = 15,
            descriptionEn = "A Victorian scientist invents a machine that can travel through time, venturing into the year 802,701 AD to discover humanity's futuristic fate.",
            descriptionHindi = "एक प्रतिभाशाली वैज्ञानिक द्वारा समय-यात्रा यंत्र का आविष्कार और वर्ष 802,701 ईस्वी में भविष्य की दुनिया का हैरतअंगेज सफर।",
            gutenbergOrOpenLibraryUrl = "https://www.gutenberg.org/ebooks/35",
            tags = listOf("Sci-Fi", "Time Travel", "Classic", "Adventure"),
            chapters = listOf(
                BookChapter(
                    chapterNumber = 1,
                    title = "Chapter 1: The Fourth Dimension of Space",
                    titleHindi = "अध्याय 1: समय — अंतरिक्ष का चौथा आयाम",
                    summaryHindi = "टाइम ट्रैवलर अपने दोस्तों को समझाते हैं कि लंबाई, चौड़ाई और ऊंचाई के अलावा समय भी एक आयाम है जिसमें गति संभव है।",
                    content = """The Time Traveller (for so it will be convenient to speak of him) was expounding a recondite matter to us. His grey eyes shone and twinkled, and his usually pale face was flushed and animated. The fire burned brightly, and the soft radiance of the incandescent lights in the lilies of silver caught the bubbles that flashed and passed in our glasses.

'You must follow me carefully. I shall have to controvert one or two ideas that are almost universally accepted. The geometry, for instance, they taught you at school is founded on a misconception.'

'Is not that rather a large thing to expect us to begin upon?' said Filby, an argumentative person with red hair.

'I do not mean to ask you to accept anything without reasonable ground for it. You will soon admit as much as I need from you. You know of course that a mathematical line, a line of thickness nil, has no real existence. They taught you that? Neither has a mathematical plane. These things are mere abstractions.'

'That is all right,' said the Psychologist.

'Nor, having only length, breadth, and thickness, can a cube have a real existence.'

'There I object,' said Filby. 'Of course a solid body may exist. All real things are three-dimensional.'

'So most people think. But wait a moment. Can an instantaneous cube exist?'

'Don't follow you,' said Filby.

'Can a cube that does not exist for any time at all, have a real existence?'

Filby became pensive. 'Clearly,' the Time Traveller proceeded, 'any real body must have extension in four directions: it must have Length, Breadth, Thickness, and—Duration. But through a natural infirmity of the flesh, which I will explain to you in a moment, we incline to overlook this fact. There is no difference between Time and any of the three dimensions of Space except that our consciousness moves along it.'""",
                    keyVocabulary = listOf(
                        BookWordGlossary("Recondite", "गूढ़ / गहरा और जटिल", "रिकॉन्डाइट", "He explained a recondite scientific philosophy."),
                        BookWordGlossary("Controvert", "तथ्यों का खंडन करना", "कॉन्ट्रोवर्ट", "He tried to controvert the accepted theory."),
                        BookWordGlossary("Pensive", "गहन सोच में डूबा हुआ", "पेंसिव", "Filby fell into a pensive mood.")
                    )
                )
            )
        )
    )
}
