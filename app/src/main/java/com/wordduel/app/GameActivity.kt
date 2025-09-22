package com.wordduel.app

import android.R.attr.padding
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wordduel.app.databinding.ActivityGameBinding
//import kotlin.random.Random
import kotlin.random.Random
class GameActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameBoard: Array<Array<TextView>>
    private lateinit var keyboardButtons: MutableMap<Char, MaterialButton>
    
    private var currentWord = ""
    private var currentGuess = ""
    private var attempts = 0
    private var gameWon = false
    private var gameOver = false
    private var usedWords = mutableSetOf<String>()
    
    // Word list (will be expanded)
    private val words = listOf(
        "hello", "world", "apple", "beach", "chair", "dance", "eagle", "flame", 
        "grape", "house", "image", "juice", "knife", "lemon", "music", "night",
        "ocean", "peace", "queen", "river", "smile", "table", "unity", "voice",
        "water", "youth", "zebra", "brain", "cloud", "dream", "earth", "faith",
        "crate",
        //**************************************************************
        "about", "above", "abuse", "actor", "acute", "admit", "adopt", "adult", "after", "again",
        "agent", "agree", "ahead", "alarm", "album", "alert", "alike", "alive", "allow", "alone",
        "along", "alter", "among", "anger", "angle", "angry", "apart", "apple", "apply", "arena",
        "argue", "arise", "array", "aside", "asset", "audio", "audit", "avoid", "award", "aware",
        "badly", "baker", "bases", "basic", "beach", "began", "begin", "begun", "being", "below",
        "bench", "billy", "birth", "black", "blame", "blind", "block", "blood", "board", "boost",
        "booth", "bound", "brain", "brand", "bread", "break", "breed", "brief", "bring", "broad",
        "broke", "brown", "build", "built", "buyer", "cable", "calif", "carry", "catch", "cause",
        "chain", "chair", "chart", "chase", "cheap", "check", "chest", "chief", "child", "china",
        "chose", "civil", "claim", "class", "clean", "clear", "click", "climb", "clock", "close",
        "coach", "coast", "could", "count", "court", "cover", "craft", "crash", "cream", "crime",
        "cross", "crowd", "crown", "curve", "cycle", "daily", "dance", "dated", "dealt", "death",
        "debut", "delay", "depth", "doing", "doubt", "dozen", "draft", "drama", "drawn", "dream",
        "dress", "drill", "drink", "drive", "drove", "dying", "eager", "early", "earth", "eight",
        "elite", "empty", "enemy", "enjoy", "enter", "entry", "equal", "error", "event", "every",
        "exact", "exist", "extra", "faith", "false", "fault", "fiber", "field", "fifth", "fifty",
        "fight", "final", "first", "fixed", "flash", "fleet", "floor", "fluid", "focus", "force",
        "forth", "forty", "forum", "found", "frame", "frank", "fraud", "fresh", "front", "fruit",
        "fully", "funny", "giant", "given", "glass", "globe", "going", "grace", "grade", "grand",
        "grant", "grass", "great", "green", "gross", "group", "grown", "guard", "guess", "guest",
        "guide", "happy", "harry", "heart", "heavy", "hence", "henry", "horse", "hotel", "house",
        "human", "ideal", "image", "index", "inner", "input", "issue", "japan", "jimmy", "joint",
        "jones", "judge", "known", "label", "large", "laser", "later", "laugh", "layer", "learn",
        "lease", "least", "leave", "legal", "level", "lewis", "light", "limit", "links", "lives",
        "local", "loose", "lower", "lucky", "lunch", "lying", "magic", "major", "maker", "march",
        "maria", "match", "maybe", "mayor", "meant", "media", "metal", "might", "minor", "minus",
        "mixed", "model", "money", "month", "moral", "motor", "mount", "mouse", "mouth", "moved",
        "movie", "music", "needs", "never", "newly", "night", "noise", "north", "noted", "novel",
        "nurse", "occur", "ocean", "offer", "often", "order", "other", "ought", "paint", "panel",
        "paper", "party", "peace", "peter", "phase", "phone", "photo", "piece", "pilot", "pitch",
        "place", "plain", "plane", "plant", "plate", "point", "pound", "power", "press", "price",
        "pride", "prime", "print", "prior", "prize", "proof", "proud", "prove", "queen", "quick",
        "quiet", "quite", "radio", "raise", "range", "rapid", "ratio", "reach", "ready", "realm",
        "rebel", "refer", "relax", "relay", "renal", "renew", "repay", "reply", "rerun", "reset",
        "retry", "revel", "rhine", "rhyme", "rider", "ridge", "rifle", "right", "rigid", "rigor",
        "rival", "river", "robin", "roger", "roman", "rough", "round", "route", "rover", "royal",
        "rural", "safer", "saint", "salem", "sally", "salon", "satin", "sauce", "saver", "scale",
        "scare", "scarf", "scary", "scene", "scent", "scoff", "scold", "scoop", "scope", "score",
        "scorn", "scout", "scrap", "screw", "scrub", "scrum", "scuba", "seamy", "seedy", "seemly",
        "segue", "seize", "sense", "sepia", "serif", "serum", "serve", "seven", "shade", "shaft",
        "shake", "shaky", "shall", "shalt", "shame", "shank", "shape", "shard", "share", "shark",
        "sharp", "shave", "shawl", "shear", "sheen", "sheep", "sheer", "sheet", "sheik", "shelf",
        "shell", "shied", "shift", "shill", "shine", "shiny", "shire", "shirk", "shirt", "shoal",
        "shock", "shoed", "shoes", "shone", "shook", "shoot", "shore", "shorn", "short", "shout",
        "shove", "shown", "showy", "shred", "shrew", "shrub", "shrug", "shuck", "shun", "shush",
        "shut", "shyly", "sieve", "sift", "sigh", "sight", "sigma", "sign", "silk", "silky",
        "silly", "silo", "silt", "sinew", "singe", "singer", "single", "sinker", "sinus", "sip",
        "sire", "siren", "sissy", "sister", "sitcom", "sixer", "sixth", "sixty", "size", "sizer",
        "skate", "skew", "skid", "skier", "skies", "skiff", "skill", "skim", "skimp", "skin",
        "skint", "skirt", "skit", "skulk", "skull", "skunk", "slab", "slack", "slain", "slake",
        "slang", "slant", "slap", "slash", "slate", "slaw", "slay", "sled", "sleek", "sleep",
        "sleet", "slept", "slew", "slice", "slick", "slide", "slime", "slimy", "sling", "slink",
        "sly", "slip", "slit", "sliver", "slob", "slog", "slop", "slope", "slosh", "sloth",
        "slouch", "slough", "sloven", "slow", "slue", "slug", "sluice", "slum", "slump", "slung",
        "slunk", "slur", "slurp", "slush", "sly", "slyly", "smack", "small", "smart", "smash",
        "smear", "smell", "smelt", "smile", "smirk", "smite", "smith", "smithy", "smock", "smog",
        "smoke", "smoky", "smug", "smut", "snack", "snag", "snail", "snake", "snaky", "snap",
        "snare", "snarl", "snatch", "sneak", "sneer", "snide", "sniff", "snipe", "sniper", "snit",
        "snoop", "snoot", "snore", "snort", "snout", "snow", "snub", "snuck", "snuff", "snug",
        "snugly", "speak", "speed", "spell", "spend", "spent", "spew", "spice", "spicy", "spied",
        "spiel", "spike", "spiky", "spill", "spilt", "spin", "spine", "spiny", "spire", "spite",
        "spit", "splat", "splay", "splint", "split", "splotch", "splurge", "splurt", "spoil",
        "spoke", "spoof", "spook", "spool", "spoon", "spore", "sport", "spot", "spout", "sprain",
        "spray", "spree", "sprig", "spring", "sprout", "spruce", "sprung", "spry", "spud", "spun",
        "spunk", "spurn", "spurt", "sputter", "squall", "square", "squash", "squat", "squawk",
        "squeak", "squeal", "squeeze", "squib", "squid", "squiggle", "squint", "squire", "squirt",
        "squish", "stab", "stack", "staff", "stag", "stage", "stain", "stair", "stake", "stale",
        "stalk", "stall", "stamp", "stand", "stank", "stare", "stark", "stash", "state", "stave",
        "stays", "stead", "steak", "steal", "steam", "steed", "steel", "steep", "steer", "stem",
        "step", "stew", "stick", "stiff", "still", "stilt", "sting", "stink", "stint", "stir",
        "stock", "stoic", "stoke", "stole", "stomp", "stone", "stony", "stood", "stool", "stoop",
        "stop", "stops", "store", "stork", "storm", "story", "stout", "stove", "stow", "stows",
        "strap", "straw", "stray", "strep", "strew", "stria", "strip", "strop", "strum", "strut",
        "stub", "stuck", "stud", "study", "stuff", "stump", "stung", "stunk", "stuns", "stunt",
        "style", "styli", "suave", "suede", "sugar", "suit", "suite", "sulfa", "sulk", "sulky",
        "sully", "sultry", "sumac", "sums", "sunny", "super", "surer", "surf", "surge", "surl",
        "surly", "sushi", "sutra", "swab", "swad", "swag", "swage", "swain", "swale", "swam",
        "swami", "swamp", "swampy", "swan", "swank", "swans", "swap", "swarm", "swash", "swat",
        "swath", "sway", "swear", "sweat", "sweep", "swept", "swig", "swill", "swim", "swipe",
        "swirl", "swish", "swiss", "swoon", "swoop", "swop", "sword", "swore", "sworn", "swum",
        "swung", "syrup", "tabby", "tabla", "table", "taboo", "tacit", "tack", "tacky", "taco",
        "tact", "taffy", "tag", "tail", "taint", "take", "taken", "taker", "takes", "tally",
        "talon", "tamer", "tango", "tangy", "taper", "tapir", "tardy", "tarot", "tarry", "task",
        "taste", "tasty", "tatty", "taunt", "tawny", "taxi", "teach", "tear", "tease", "teens",
        "teeth", "tempo", "tenet", "tenor", "tense", "tenth", "tepee", "tepid", "term", "tern",
        "terse", "test", "testy", "tether", "thank", "theft", "their", "theme", "then", "there",
        "these", "theta", "thick", "thief", "thigh", "thing", "think", "third", "thong", "thorn",
        "those", "thou", "thread", "three", "threw", "throb", "throw", "thrum", "thud", "thug",
        "thumb", "thump", "thunk", "thus", "thyme", "tiara", "tibia", "tidal", "tidy", "tie",
        "tied", "tier", "ties", "tiger", "tight", "tilde", "timer", "timid", "timmy", "timon",
        "tint", "tiny", "tip", "tiptoe", "tiptop", "tire", "tired", "toad", "toast", "today",
        "toddy", "toe", "tofu", "toga", "toil", "toilet", "token", "told", "tommy", "tonal",
        "tone", "tonga", "tonic", "tonne", "tool", "toon", "tooth", "topaz", "topic", "torah",
        "torch", "torso", "torus", "total", "totem", "touch", "tough", "towel", "tower", "town",
        "toxic", "toxin", "toy", "trace", "track", "tract", "trade", "trail", "train", "trait",
        "tram", "trance", "trap", "trash", "trawl", "tray", "tread", "treat", "treble", "tree",
        "trek", "trend", "triad", "trial", "tribe", "trice", "trick", "tried", "trifle", "trill",
        "trim", "trinity", "trio", "trip", "tripe", "trite", "troll", "troop", "trot", "trout",
        "truce", "truck", "trudge", "trump", "trunk", "truss", "trust", "truth", "try", "tryst",
        "tubal", "tubby", "tube", "tubing", "tuck", "tug", "tuition", "tulip", "tulle", "tumor",
        "tumult", "tuna", "tundra", "tune", "tunic", "tunnel", "tupelo", "turban", "turbid", "turbine",
        "turf", "turgid", "turkey", "turmoil", "turn", "turnip", "turret", "turtle", "tusk", "tussle",
        "tutor", "tutu", "tux", "twang", "tweak", "tweed", "tweet", "twelve", "twenty", "twice",
        "twiddle", "twig", "twilight", "twill", "twin", "twine", "twirl", "twist", "twisty", "twitch",
        "twitter", "two", "tycoon", "tying", "tyke", "udder", "ulcer", "ulna", "ultra", "umber",
        "umbra", "umpire", "uncle", "uncut", "under", "undid", "undo", "undone", "undue", "unfed",
        "unfit", "unify", "union", "unite", "unity", "unlit", "unmet", "unmoved", "unpack", "unpaid",
        "unquote", "unrest", "unsaid", "unseen", "unsent", "untidy", "untie", "until", "untrue",
        "unused", "unusual", "unveil", "unwind", "unwon", "unzip", "upbeat", "upchuck", "upcoming",
        "update", "upend", "upgrade", "uphold", "upkeep", "upland", "uplift", "upload", "upon", "upper",
        "upright", "upriver", "uproar", "uproot", "upset", "upshot", "upside", "upstage", "upstairs",
        "upstart", "upstate", "upstream", "upstroke", "upswing", "uptake", "uptick", "uptight",
        "uptown", "upturn", "upward", "upwind", "urban", "urchin", "urine", "usable", "usage",
        "use", "used", "useful", "useless", "user", "usher", "usual", "usurp", "utensil", "utility",
        "utilize", "utmost", "utopia", "utter", "uvula", "vacant", "vacate", "vacation", "vaccine",
        "vacuum", "vagabond", "vagrant", "vague", "valet", "valid", "valley", "valor", "value",
        "valve", "vamp", "vampire", "vane", "vapor", "vary", "vase", "vast", "vault", "vaunt",
        "veal", "vegan", "veggie", "veil", "vein", "velar", "veldt", "vella", "velvet", "venal",
        "vend", "vendor", "veneer", "venial", "venom", "venous", "vent", "ventral", "venue", "venus",
        "veranda", "verb", "verbal", "verbose", "verdict", "verge", "vermin", "vernal", "verse",
        "version", "verso", "versus", "vertex", "vertical", "vertigo", "verve", "very", "vesper",
        "vessel", "vest", "vestal", "vestibule", "vestige", "vestment", "vestry", "vet", "vetch",
        "veto", "vex", "vexed", "vexing", "viable", "viaduct", "vial", "viand", "vibrant", "vibrate",
        "vicar", "vice", "viceroy", "vicinity", "vicious", "victim", "victor", "victory", "victual",
        "video", "vie", "view", "viewer", "vigil", "vigilant", "vigor", "vigorous", "vile", "vilify",
        "villa", "village", "villain", "vindicate", "vine", "vinegar", "vineyard", "vintage", "viola",
        "violate", "violent", "violet", "violin", "viper", "viral", "virgin", "virile", "virtual",
        "virtue", "virtuous", "virus", "visa", "viscount", "visible", "vision", "visit", "visitor",
        "visor", "vista", "visual", "vital", "vitamin", "vivacious", "vivid", "vixen", "vizier",
        "vocab", "vocal", "vocalist", "vocation", "vocative", "vodka", "vogue", "voice", "voiced",
        "void", "voile", "vol", "volatile", "volcanic", "volley", "volt", "voltage", "voltaic",
        "volume", "voluntary", "volunteer", "vomit", "voodoo", "voracious", "vortex", "votary",
        "vote", "voter", "votive", "vouch", "vow", "vowel", "vulgar", "vulva", "vying", "wacky",
        "wafer", "wager", "wages", "wagon", "waif", "waist", "wait", "waive", "waiver", "wake",
        "waken", "walk", "walker", "walking", "walkout", "walkup", "wall", "wallet", "wallop",
        "wallow", "wallpaper", "walnut", "walrus", "waltz", "wand", "wander", "wane", "want",
        "wanton", "war", "ward", "warden", "wardrobe", "ware", "warehouse", "warfare", "warlike",
        "warlock", "warlord", "warm", "warmth", "warn", "warp", "warrant", "warren", "warrior",
        "wart", "wary", "was", "wash", "washer", "washout", "wasp", "wastage", "waste", "waster",
        "watch", "watcher", "water", "watery", "watt", "watts", "wave", "waver", "wavy", "wax",
        "waxen", "waxing", "way", "waylay", "ways", "wayward", "weak", "weaken", "weakling",
        "weakness", "weal", "wealth", "wealthy", "wean", "weapon", "weaponry", "wear", "weary",
        "weasel", "weather", "weave", "weaver", "web", "webbed", "webbing", "wed", "wedding",
        "wedge", "wedlock", "wee", "weed", "weedy", "week", "weekday", "weekend", "weekly", "weep",
        "weeping", "weepy", "weigh", "weight", "weighty", "weird", "weirdo", "welcome", "weld",
        "welder", "welfare", "well", "welsh", "welt", "welter", "wench", "wend", "went", "wept",
        "were", "wert", "west", "western", "wet", "wetland", "whack", "whale", "whaler", "wham",
        "wharf", "what", "whatever", "whatnot", "wheat", "whee", "wheel", "wheeze", "wheezy",
        "whelk", "whelm", "whelp", "when", "whence", "whenever", "where", "whereas", "whereby",
        "wherein", "whereof", "whereon", "wherever", "wherewith", "whet", "whether", "whew",
        "whey", "which", "whichever", "whiff", "while", "whilst", "whim", "whimper", "whimsy",
        "whine", "whinny", "whip", "whiplash", "whippet", "whir", "whirl", "whirlpool", "whirlwind",
        "whisk", "whisker", "whiskey", "whisper", "whistle", "whit", "white", "whiten", "whither",
        "whiting", "whitish", "whittle", "whiz", "whizz", "who", "whoa", "whoever", "whole",
        "wholesale", "wholesome", "whom", "whomever", "whoop", "whoosh", "whop", "whopper",
        "whopping", "whore", "whose", "whosoever", "why", "wick", "wicked", "wicker", "wicket",
        "wide", "widen", "wider", "widespread", "widow", "width", "wield", "wiener", "wife",
        "wig", "wiggle", "wiggly", "wight", "wild", "wildcat", "wilderness", "wildfire", "wildfowl",
        "wildlife", "wildly", "wile", "will", "willing", "willow", "wilt", "wily", "wimp",
        "wimpy", "win", "wince", "winch", "wind", "windage", "windblown", "windburn", "winder",
        "windfall", "windlass", "windmill", "window", "windpipe", "windproof", "windrow",
        "windstorm", "windsurf", "windswept", "windup", "windward", "windy", "wine", "winery",
        "wing", "wingding", "winged", "winging", "wingless", "winglet", "wingman", "wingover",
        "wink", "winker", "winkle", "winner", "winning", "winnow", "wino", "winsome", "winter",
        "winterize", "wintertime", "wintry", "wipe", "wire", "wired", "wireless", "wiretap",
        "wiring", "wiry", "wisdom", "wise", "wish", "wishbone", "wishful", "wishy", "wisp",
        "wispy", "wistful", "wit", "witch", "with", "withdraw", "withdrawal", "withdrawn", "withdrew",
        "wither", "withheld", "withhold", "within", "without", "withstand", "withstood", "witness",
        "witnessed", "witnessing", "wits", "witty", "wive", "wives", "wizard", "wizened", "wobble",
        "wobbly", "woe", "woebegone", "woeful", "wok", "woke", "woken", "wolf", "wolfhound",
        "wolfish", "wolfram", "wolverine", "woman", "womanhood", "womanish", "womanlike", "womanly",
        "womb", "wombat", "women", "won", "wonder", "wonderful", "wondrous", "wont", "woo",
        "wood", "woodbine", "woodchuck", "woodcock", "woodcut", "wooded", "wooden", "woodland",
        "woodlot", "woodman", "woodpecker", "woodpile", "woodruff", "woods", "woodshed", "woodsy",
        "woodwind", "woodwork", "woody", "wooer", "woof", "wool", "woolen", "woolgathering",
        "woolly", "woozy", "word", "wordage", "wordbook", "wordless", "wordplay", "words", "wordy",
        "work", "workable", "workaday", "workbag", "workbook", "workday", "worker", "workhorse",
        "workhouse", "working", "workload", "workman", "workmanlike", "workout", "workplace",
        "workroom", "works", "worksheet", "workshop", "worktable", "workup", "world", "worldly",
        "worldwide", "worm", "wormy", "worn", "worried", "worry", "worrying", "worse", "worsen",
        "worship", "worst", "worth", "worthless", "worthwhile", "worthy", "would", "wound",
        "wove", "woven", "wow", "wrack", "wraith", "wrangle", "wrap", "wrapper", "wrapping",
        "wrath", "wreak", "wreath", "wreathe", "wreck", "wreckage", "wrecker", "wrecking",
        "wren", "wrench", "wrest", "wrestle", "wrestler", "wretch", "wretched", "wriggle",
        "wriggly", "wright", "wring", "wringer", "wringing", "wrinkle", "wrinkly", "wrist",
        "wristband", "writ", "write", "writer", "writhe", "writing", "written", "wrong", "wrongdoer",
        "wrongdoing", "wrongful", "wrongheaded", "wrongly", "wrongness", "wrote", "wrought",
        "wrung", "wry", "wryly", "wryness", "wuss", "xerox", "yacht", "yack", "yadda", "yank",
        "yap", "yard", "yardage", "yardarm", "yardstick", "yarn", "yaw", "yawl", "yawn", "yawning",
        "yawp", "yaws", "ye", "yea", "yeah", "year", "yearbook", "yearling", "yearly", "yearn",
        "yearning", "yeast", "yeasty", "yell", "yellow", "yelp", "yen", "yeoman", "yes",
        "yesterday", "yesteryear", "yet", "yew", "yield", "yielding", "yikes", "yip", "yodel",
        "yoga", "yogi", "yoke", "yokel", "yolk", "yonder", "yore", "you", "young", "younger",
        "youngest", "youngish", "youngster", "your", "yours", "yourself", "yourselves", "youth",
        "youthful", "yowl", "yoyo", "yuck", "yucky", "yuk", "yule", "yummy", "yuppie", "yurt",
        "zany", "zap", "zapper", "zappy", "zeal", "zealot", "zealous", "zebra", "zen", "zenith",
        "zephyr", "zeppelin", "zero", "zest", "zesty", "zeta", "zigzag", "zilch", "zillion",
        "zinc", "zincic", "zing", "zingy", "zinnia", "zip", "zipper", "zippy", "zircon", "zit",
        "zither", "zloty", "zodiac", "zombie", "zonal", "zone", "zoned", "zoning", "zonk",
        "zoo", "zoological", "zoologist", "zoology", "zoom", "zoophyte", "zooplankton", "zoospore",
        "zoot", "zowie", "zucchini", "zurich", "zwieback", "zydeco", "zygote", "zygotic", "zymurgy",

        // Additional common 5-letter words
        "abide", "abode", "abort", "abuse", "acids", "acorn", "acres", "acted", "added", "adobe",
        "adopt", "adult", "after", "again", "agent", "agree", "ahead", "alarm", "album", "alert",
        "alike", "alive", "allow", "alone", "along", "alter", "among", "anger", "angle", "angry",
        "apart", "apple", "apply", "arena", "argue", "arise", "array", "aside", "asset", "audio",
        "audit", "avoid", "award", "aware", "badly", "baker", "bases", "basic", "beach", "began",
        "begin", "begun", "being", "below", "bench", "birth", "black", "blame", "blind", "block",
        "blood", "board", "boost", "booth", "bound", "brain", "brand", "bread", "break", "breed",
        "brief", "bring", "broad", "broke", "brown", "build", "built", "buyer", "cable", "carry",
        "catch", "cause", "chain", "chair", "chart", "chase", "cheap", "check", "chest", "chief",
        "child", "china", "chose", "civil", "claim", "class", "clean", "clear", "click", "climb",
        "clock", "close", "coach", "coast", "could", "count", "court", "cover", "craft", "crash",
        "cream", "crime", "cross", "crowd", "crown", "curve", "cycle", "daily", "dance", "dated",
        "dealt", "death", "debut", "delay", "depth", "doing", "doubt", "dozen", "draft", "drama",
        "drawn", "dream", "dress", "drill", "drink", "drive", "drove", "dying", "eager", "early",
        "earth", "eight", "elite", "empty", "enemy", "enjoy", "enter", "entry", "equal", "error",
        "event", "every", "exact", "exist", "extra", "faith", "false", "fault", "fiber", "field",
        "fifth", "fifty", "fight", "final", "first", "fixed", "flash", "fleet", "floor", "fluid",
        "focus", "force", "forth", "forty", "forum", "found", "frame", "frank", "fraud", "fresh",
        "front", "fruit", "fully", "funny", "giant", "given", "glass", "globe", "going", "grace",
        "grade", "grand", "grant", "grass", "great", "green", "gross", "group", "grown", "guard",
        "guess", "guest", "guide", "happy", "heart", "heavy", "hence", "horse", "hotel", "house",
        "human", "ideal", "image", "index", "inner", "input", "issue", "japan", "joint", "judge",
        "known", "label", "large", "laser", "later", "laugh", "layer", "learn", "lease", "least",
        "leave", "legal", "level", "light", "limit", "links", "lives", "local", "loose", "lower",
        "lucky", "lunch", "lying", "magic", "major", "maker", "march", "match", "maybe", "mayor",
        "meant", "media", "metal", "might", "minor", "minus", "mixed", "model", "money", "month",
        "moral", "motor", "mount", "mouse", "mouth", "moved", "movie", "music", "needs", "never",
        "newly", "night", "noise", "north", "noted", "novel", "nurse", "occur", "ocean", "offer",
        "often", "order", "other", "ought", "paint", "panel", "paper", "party", "peace", "phase",
        "phone", "photo", "piece", "pilot", "pitch", "place", "plain", "plane", "plant", "plate",
        "point", "pound", "power", "press", "price", "pride", "prime", "print", "prior", "prize",
        "proof", "proud", "prove", "queen", "quick", "quiet", "quite", "radio", "raise", "range",
        "rapid", "ratio", "reach", "ready", "realm", "rebel", "refer", "relax", "relay", "renew",
        "repay", "reply", "reset", "retry", "revel", "rhyme", "rider", "ridge", "rifle", "right",
        "rigid", "rigor", "rival", "river", "rough", "round", "route", "royal", "rural", "safer",
        "saint", "salon", "satin", "sauce", "saver", "scale", "scare", "scarf", "scary", "scene",
        "scent", "scoff", "scold", "scoop", "scope", "score", "scorn", "scout", "scrap", "screw",
        "scrub", "serve", "seven", "shade", "shaft", "shake", "shaky", "shall", "shame", "shape",
        "share", "sharp", "shave", "shear", "sheen", "sheep", "sheet", "shelf", "shell", "shift",
        "shine", "shiny", "shirt", "shock", "shoes", "shook", "shoot", "shore", "short", "shout",
        "shown", "shred", "shrub", "shrug", "shut", "sieve", "sight", "sigma", "silly", "sinew",
        "singer", "sinker", "siren", "sissy", "sister", "sixth", "sixty", "skate", "skill", "skin",
        "skirt", "skull", "slack", "slain", "slang", "slant", "slate", "slay", "sleep", "slept",
        "slice", "slide", "slime", "sling", "slip", "slit", "slob", "slope", "sloth", "slow",
        "slug", "slum", "slump", "slung", "slunk", "slur", "slush", "smack", "small", "smart",
        "smash", "smear", "smell", "smile", "smirk", "smith", "smock", "smog", "smoke", "smoky",
        "smug", "snack", "snag", "snail", "snake", "snap", "snare", "snarl", "sneak", "sneer",
        "snide", "sniff", "snipe", "snoop", "snore", "snort", "snow", "snub", "snuck", "snuff",
        "snug", "speak", "speed", "spell", "spend", "spent", "spice", "spicy", "spied", "spike",
        "spill", "spilt", "spin", "spine", "spiny", "spire", "spite", "splat", "split", "spoil",
        "spoke", "spoof", "spook", "spool", "spoon", "spore", "sport", "spot", "spout", "spray",
        "spree", "sprig", "spring", "sprout", "spruce", "spry", "spun", "spunk", "spurn", "spurt",
        "square", "squash", "squat", "squawk", "squeak", "squib", "squid", "squint", "squire",
        "stab", "stack", "staff", "stage", "stain", "stair", "stake", "stale", "stalk", "stall",
        "stamp", "stand", "stank", "stare", "stark", "stash", "state", "stave", "stays", "stead",
        "steak", "steal", "steam", "steed", "steel", "steep", "steer", "stem", "step", "stew",
        "stick", "stiff", "still", "stilt", "sting", "stink", "stint", "stir", "stock", "stoic",
        "stoke", "stole", "stomp", "stone", "stony", "stood", "stool", "stoop", "stop", "store",
        "stork", "storm", "story", "stout", "stove", "strap", "straw", "stray", "strip", "strut",
        "stub", "stuck", "stud", "study", "stuff", "stump", "stung", "stunk", "stunt", "style",
        "suave", "sugar", "suite", "sulk", "sulky", "sunny", "super", "surf", "surge", "surly",
        "swab", "swag", "swam", "swamp", "swan", "swap", "swarm", "swat", "sway", "swear", "sweep",

        // Add missing common words that were in Android version
        "hello", "eagle", "flame",

        // Additional common 5-letter words
        "spark", "snark", "start", "crate", "bingo", "plaid", "freak", "leery", "berry"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        initializeGame()
        setupClickListeners()
    }
    
    private fun setupUI() {
        supportActionBar?.hide()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        
        createGameBoard()
        createKeyboard()
    }
    
    private fun createGameBoard() {
        gameBoard = Array(6) { row ->
            Array(5) { col ->
                TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(4, 4, 4, 4)
                    }
                    
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 24f
                    gravity = android.view.Gravity.CENTER
                    setBackgroundResource(R.drawable.ic_launcher)
                    
                    binding.gameBoard.addView(this)
                }
            }
        }
    }
    
    private fun createKeyboard() {
        keyboardButtons = mutableMapOf()

        val keyRows = listOf(
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        )

        keyRows.forEachIndexed { rowIndex, row ->
            val rowLayout = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }

            /*           row.forEach { char ->
                val keyButton = MaterialButton(this).apply {
                    text = char.toString()
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(4, 0, 4, 0)
                    }
                    
                    setOnClickListener { onKeyPress(char) }
                    setBackgroundColor(ContextCompat.getColor(context, R.color.keyboard_key_background))
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    16f.also { textSize = it }
                    minWidth = 0
                    minHeight = 0
                    padding = 0
                    cornerRadius = 8
                }
                
                keyboardButtons[char] = keyButton
                rowLayout.addView(keyButton)
            }
            
            binding.keyboardContainer.addView(rowLayout)
        }*/
            row.forEach { char ->
                val keyButton = MaterialButton(this).apply {
                    text = char.toString()
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(4, 0, 4, 0)
                    }

                    setOnClickListener { onKeyPress(char) }
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.keyboard_key_background
                        )
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 16f // Correct way to set textSize for the MaterialButton
                    minWidth = 0
                    minHeight = 0
                    padding = 0 // Consider using setPadding(0,0,0,0) or ensure material library handles this as expected
                    cornerRadius = 8 // Make sure this is a valid property or method, for MaterialButton it's usually set via style or shapeAppearance
                }

                keyboardButtons[char] = keyButton
                rowLayout.addView(keyButton)
            }


            // Add special keys
            val specialRow = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }

            // Enter key
            val enterButton = MaterialButton(this).apply {
                text = "ENTER"
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(4, 0, 4, 0)
                }
                setOnClickListener { submitGuess() }
                setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                textSize = 14f
                minWidth = 0
                minHeight = 0
                padding = 0
                cornerRadius = 8
            }

            // Backspace key
            val backspaceButton = MaterialButton(this).apply {
                text = "⌫"
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(4, 0, 4, 0)
                }
                setOnClickListener { onBackspace() }
                setBackgroundColor(ContextCompat.getColor(context, R.color.secondary))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                textSize = 18f
                minWidth = 0
                minHeight = 0
                padding = 0
                cornerRadius = 8
            }

            specialRow.addView(enterButton)
            specialRow.addView(backspaceButton)
            binding.keyboardContainer.addView(specialRow)
        }

       private fun initializeGame() {
            currentWord = words.random()
            currentGuess = ""
            attempts = 0
            gameWon = false
            gameOver = false
            usedWords.clear()

            updateGuessCounter()
            showMessage("Computer has chosen a word. Good luck!", Snackbar.LENGTH_LONG)
        }

        private fun setupClickListeners() {
            binding.btnPlayAgain.setOnClickListener {
                startNewGame()
            }

            binding.btnBack.setOnClickListener {
                finish()
            }
        }

        private fun onKeyPress(char: Char) {
            if(gameOver || currentGuess.length >= 5) return

            currentGuess += char.lowercase()
            updateGameBoardDisplay()

            // Add key press animation
            val scaleAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            scaleAnimation.duration = 100
            keyboardButtons[char]?.startAnimation(scaleAnimation)
        }

        private fun onBackspace() {
            if(currentGuess.isNotEmpty()) {
                currentGuess = currentGuess.dropLast(1)
                updateGameBoardDisplay()
            }
        }

        private fun updateGameBoardDisplay() {
            for (i in 0 until 5) {
                val cell = gameBoard[attempts][i]
                if(i < currentGuess.length) {
                    cell.text = currentGuess[i].uppercase()
                    cell.setBackgroundResource(R.drawable.game_cell_filled)
                } else {
                    cell.text = ""
                    cell.setBackgroundResource(R.drawable.game_cell_background)
                }
            }
        }

        private fun submitGuess() {
            if(currentGuess.length != 5) {
                showMessage("Word must be 5 letters long", Snackbar.LENGTH_SHORT)
                return
            }

            if(usedWords.contains(currentGuess)) {
                showMessage("You already guessed that word!", Snackbar.LENGTH_SHORT)
                return
            }

            if(!words.contains(currentGuess)) {
                showMessage("Not a valid word", Snackbar.LENGTH_SHORT)
                return
            }

            usedWords.add(currentGuess)

            // Generate feedback and apply to board
            val feedback = generateFeedback(currentGuess, currentWord)
            applyFeedbackToBoard(attempts, currentGuess, feedback)

            // Update keyboard colors
            updateKeyboardColors(currentGuess, feedback)

            if(currentGuess == currentWord) {
                gameWon = true
                gameOver = true
                showMessage("Congratulations! You guessed the word!", Snackbar.LENGTH_LONG)
                binding.btnPlayAgain.visibility = View.VISIBLE
            } else {
                attempts++
                updateGuessCounter()

                if(attempts >= 6) {
                    gameOver = true
                    showMessage(
                        "Game over! The word was: ${currentWord.uppercase()}",
                        Snackbar.LENGTH_LONG
                    )
                    binding.btnPlayAgain.visibility = View.VISIBLE
                } else {
                    currentGuess = ""
                    showMessage("${6 - attempts} attempts remaining", Snackbar.LENGTH_SHORT)
                }
            }
        }

        private fun generateFeedback(guess: String, targetWord: String): Array<String> {
            val feedback = Array(5) { "absent" }
            val targetLetters = targetWord.toMutableList()
            val guessLetters = guess.toList()

            // First pass: mark correct letters
            for (i in 0 until 5) {
                if(guessLetters[i] == targetLetters[i]) {
                    feedback[i] = "correct"
                    targetLetters[i] = ' ' // Mark as used
                }
            }

            // Second pass: mark present letters
            for (i in 0 until 5) {
                if(feedback[i] == "correct") continue

                val letterIndex = targetLetters.indexOf(guessLetters[i])
                if(letterIndex != -1) {
                    feedback[i] = "present"
                    targetLetters[letterIndex] = ' ' // Mark as used
                }
            }

            return feedback
        }

        private fun applyFeedbackToBoard(rowIndex: Int, guess: String, feedback: Array<String>) {
            for (i in 0 until 5) {
                val cell = gameBoard[rowIndex][i]
                cell.text = guess[i].uppercase()

                val backgroundRes = when (feedback[i]) {
                    "correct" -> R.drawable.game_cell_correct
                    "present" -> R.drawable.game_cell_present
                    else -> R.drawable.game_cell_absent
                }

                cell.setBackgroundResource(backgroundRes)

                // Add feedback animation
                val pulseAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
                pulseAnimation.duration = 300
                cell.startAnimation(pulseAnimation)
            }
        }

        private fun updateKeyboardColors(guess: String, feedback: Array<String>) {
            for (i in 0 until 5) {
                val char = guess[i].uppercaseChar()
                val button = keyboardButtons[char] ?: continue

                val colorRes = when (feedback[i]) {
                    "correct" -> R.color.correct
                    "present" -> R.color.present
                    else -> R.color.absent
                }

                button.setBackgroundColor(ContextCompat.getColor(this, colorRes))
            }
        }

        private fun updateGuessCounter() {
            binding.guessCounter.text = "Guess ${attempts + 1}/6"
        }

        private fun showMessage(message: String, duration: Int) {
            Snackbar.make(binding.root, message, duration).show()
        }

        private fun startNewGame() {
            binding.btnPlayAgain.visibility = View.GONE

            // Clear game board
            for (row in gameBoard) {
                for (cell in row) {
                    cell.text = ""
                    cell.setBackgroundResource(R.drawable.game_cell_background)
                }
            }

            // Reset keyboard colors
            keyboardButtons.values.forEach { button ->
                button.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.keyboard_key_background
                    )
                )
            }

            initializeGame()
        }
    }
}

