const r3 = [
    "kya", "kyu", "kyo", "sha", "shu", "sho", "cha", "chu", "cho",
    "nya", "nyu", "nyo", "hya", "hyu", "hyo", "mya", "myu", "myo",
    "rya", "ryu", "ryo", "gya", "gyu", "gyo", "bya", "byu", "byo",
    "pya", "pyu", "pyo", "shi", "chi", "tsu" ];

const r2 = [
    "ka", "ki", "ku", "ke", "ko", "sa", "su", "se", "so",
    "ta", "te", "to", "na", "ni", "nu", "ne", "no",
    "ha", "hi", "fu", "he", "ho", "ma", "mi", "mu", "me", "mo",
    "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro",
    "wa", "wo", "ga", "gi", "gu", "ge", "go",
    "za", "ji", "zu", "ze", "zo", "da", "de", "do",
    "ba", "bi", "bu", "be", "bo", "pa", "pi", "pu", "pe", "po",
    "ja", "ju", "jo" ];

const r1 = ["a", "i", "u", "e", "o", "n"];

const artefact = [" ", ",", ".", ":", "\"", "/", "\\"];

let output = [];
let count = 0;

function double(r, text){
    let t = text;
    for(let i=0; i<r.length; i++){
        let data = r[i][0] + r[i];
        if(t.startsWith(data)){
            t = t.substring(data.length);
            const art = anti_artefact(artefact, t);
            output.push(data + art);
            count += data.length + art.length;
            t = t.substring(art.length);
        }
    }
    return t;
}

function simple(r, text){
    let t = text;
    for(let i=0; i<r.length; i++){
        let data = r[i];
        if(t.startsWith(data)){
            t = t.substring(data.length);
            const art = anti_artefact(artefact, t);
            output.push(data + art);
            count += data.length + art.length;
            t = t.substring(art.length);
        }
    }
    return t;
}

function anti_artefact(r, text){
    let t = text;
    let a = "";
    for(let i=0; i<t.length; i++){
        let data = r[i];
        if(t.startsWith(data)){
            t = t.substring(data.length);
            count += data.length;
            a += data;
        }
    }
    return a;
}

register("separate_romaji", () => {
    // On récupère le texte dans une des deux zones de texte.
    let t = api.getSentence();

    count = 0;
    let count_limits = 0;

    // Texte
    const origin_size = t.length;

    console.log("Phrase d'origine : " + t);
    console.log("Taille d'origine : " + origin_size);

    // On boucle tant que le texte n'a pas été entièrement parcouru.
    while(count < origin_size && count_limits < 300){
        count_limits++

        //console.log("Compteur : " + count);

        // On traite d'abord les doubles consonnes des syllabes japonaises.
        t = double(r3, t);
        t = double(r2, t);

        //console.log("Phrase : " + t);

        // On traite ensuite toutes les syllabes
        t = simple(r3, t);
        t = simple(r2, t);
        t = simple(r1, t);

        //console.log("Phrase : " + t);
    }

    // On réécrit la phrase
    let out= "";
    for(let i=0; i<output.length; i++){
        out += "{\\k0}" + output[i];
    }
    output = [];

    //console.log("Phrase réécrite : " + out);
    // On renvoit la chaîne
    api.setSentence(out);
});