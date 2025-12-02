package com.wordduel.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity // Added for TextView Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.wordduel.app.databinding.ActivityGameBinding // Ensure this matches your package
// import java.util.Locale // Not strictly needed if using .uppercase()
import kotlin.random.Random

// Enum for feedback - Placed outside the class for broader accessibility if needed, or can be a nested class.
enum class CharFeedback {
    CORRECT_POSITION, // Green
    WRONG_POSITION,   // Yellow
    NOT_IN_WORD       // Gray
}

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameBoard: Array<Array<TextView>>
    private lateinit var keyboardButtons: MutableMap<Char, MaterialButton>

    private var currentWord = ""
    private var currentGuess = ""
    private var attempts = 0
    private var gameWon = false
    private var gameOver = false

    private val MAX_ATTEMPTS = 6
    private val WORD_LENGTH = 5 // Consistent word length

    private lateinit var words: List<String> // This will hold the chosen word list
    private val usedWords = mutableSetOf<String>() // To track already guessed words in the current game

    // Full word list matching the web version
    private val defaultWordList = listOf(
        "ABOUT", "ABOVE", "ABUSE", "ACTOR", "ACUTE", "ADMIT", "ADOPT", "ADULT", "AFTER", "AGAIN",
        "AGENT", "AGREE", "AHEAD", "ALARM", "ALBUM", "ALERT", "ALIEN", "ALIGN", "ALIKE", "ALIVE",
        "ALLOW", "ALONE", "ALONG", "ALTER", "AMBER", "AMEND", "AMONG", "AMPLE", "AMUSE", "ANGEL",
        "ANGER", "ANGLE", "ANGRY", "APART", "APPLE", "APPLY", "ARENA", "ARGUE", "ARISE", "ARMOR",
        "ARRAY", "ARROW", "ASIDE", "ASSET", "ATLAS", "AUDIO", "AUDIT", "AVOID", "AWARD", "AWARE",
        "BADLY", "BAKER", "BASES", "BASIC", "BASIN", "BASIS", "BEACH", "BEGAN", "BEGIN", "BEGUN",
        "BEING", "BELOW", "BENCH", "BERRY", "BILLY", "BIRTH", "BLACK", "BLADE", "BLAME", "BLANK",
        "BLAST", "BLEED", "BLEND", "BLESS", "BLIND", "BLINK", "BLOCK", "BLOOD", "BLOOM", "BLOWN",
        "BOARD", "BOOST", "BOOTH", "BOUND", "BRAIN", "BRAKE", "BRAND", "BRASS", "BRAVE", "BREAD",
        "BREAK", "BREED", "BRICK", "BRIDE", "BRIEF", "BRING", "BRINK", "BRISK", "BROAD", "BROKE",
        "BROOK", "BROWN", "BRUSH", "BUILD", "BUILT", "BUNCH", "BURST", "BUYER", "CABIN", "CABLE",
        "CACHE", "CAMEL", "CANAL", "CANDY", "CANOE", "CANON", "CARGO", "CARRY", "CARVE", "CATCH",
        "CATER", "CAUSE", "CEDAR", "CHAIN", "CHAIR", "CHALK", "CHAMP", "CHAOS", "CHARM", "CHART",
        "CHASE", "CHEAP", "CHEAT", "CHECK", "CHEEK", "CHEER", "CHESS", "CHEST", "CHIEF", "CHILD",
        "CHILL", "CHINA", "CHIPS", "CHOSE", "CHUNK", "CIVIC", "CIVIL", "CLAIM", "CLAMP", "CLASH",
        "CLASP", "CLASS", "CLEAN", "CLEAR", "CLERK", "CLICK", "CLIFF", "CLIMB", "CLING", "CLOAK",
        "CLOCK", "CLONE", "CLOSE", "CLOTH", "CLOUD", "CLOWN", "COACH", "COAST", "CORAL", "COUCH",
        "COULD", "COUNT", "COURT", "COVER", "CRACK", "CRAFT", "CRAMP", "CRANE", "CRANK", "CRASH",
        "CRATE", "CRAVE", "CRAZY", "CREAK", "CREAM", "CREED", "CREEK", "CREEP", "CREST", "CRIME",
        "CRISP", "CROOK", "CROSS", "CROWD", "CROWN", "CRUDE", "CRUEL", "CRUSH", "CRUST", "CURVE",
        "CYCLE", "DAILY", "DAIRY", "DAISY", "DANCE", "DATED", "DEALS", "DEALT", "DEATH", "DEBUT",
        "DECAY", "DECOR", "DECOY", "DELAY", "DELTA", "DENSE", "DEPOT", "DEPTH", "DERBY", "DEVIL",
        "DIARY", "DOING", "DONOR", "DOUBT", "DOUGH", "DOVER", "DOWRY", "DOZEN", "DRAFT", "DRAIN",
        "DRAKE", "DRAMA", "DRANK", "DRAPE", "DRAWN", "DREAD", "DREAM", "DRESS", "DRIED", "DRIFT",
        "DRILL", "DRINK", "DRIVE", "DRONE", "DROOP", "DROWN", "DROVE", "DRUMS", "DRUNK", "DRYER",
        "DUCKS", "DUMMY", "DUMPS", "DUSTY", "DUTCH", "DWARF", "DWELL", "DYING", "EAGER", "EAGLE",
        "EARLY", "EARTH", "EASEL", "EIGHT", "ELBOW", "ELDER", "ELECT", "ELITE", "EMPTY", "ENEMY",
        "ENJOY", "ENTER", "ENTRY", "EQUAL", "EQUIP", "ERASE", "ERECT", "ERROR", "ESSAY", "ETHIC",
        "EVOKE", "EXACT", "EXAMS", "EXCEL", "EXERT", "EXILE", "EXIST", "EXTRA", "FABLE", "FACED",
        "FACTO", "FACTS", "FADED", "FAILS", "FAINT", "FAIRY", "FAITH", "FALSE", "FANCY", "FATAL",
        "FATTY", "FAULT", "FAUNA", "FAVOR", "FEAST", "FENCE", "FERRY", "FETAL", "FETCH", "FEVER",
        "FIBER", "FIELD", "FIERY", "FIFTH", "FIFTY", "FIGHT", "FILMS", "FILTH", "FINAL", "FINER",
        "FIRST", "FIXED", "FLAGS", "FLAKE", "FLAME", "FLANK", "FLARE", "FLASH", "FLASK", "FLEET",
        "FLESH", "FLICK", "FLIES", "FLING", "FLINT", "FLOAT", "FLOCK", "FLOOD", "FLOOR", "FLORA",
        "FLOUR", "FLOWS", "FLUID", "FLUNG", "FLUSH", "FLUTE", "FOAMY", "FOCAL", "FOCUS", "FOGGY",
        "FOLDS", "FOLKS", "FONTS", "FORCE", "FORGE", "FORMS", "FORTH", "FORTY", "FORUM", "FOUND",
        "FRAME", "FRANK", "FRAUD", "FREAK", "FREED", "FRESH", "FRIED", "FRIES", "FRILL", "FRISK",
        "FRONT", "FROST", "FROWN", "FROZE", "FRUIT", "FUELS", "FULLY", "FUNDS", "FUNNY", "FUZZY",
        "GAINS", "GAMER", "GAMMA", "GASES", "GATES", "GAUGE", "GAVEL", "GEARS", "GEESE", "GENES",
        "GENRE", "GHOST", "GIANT", "GIFTS", "GIRLS", "GIVEN", "GIVER", "GLAND", "GLASS", "GLAZE",
        "GLEAM", "GLIDE", "GLINT", "GLOBE", "GLOOM", "GLORY", "GLOSS", "GLOVE", "GLUED", "GOALS",
        "GOATS", "GODLY", "GOING", "GOODS", "GOOSE", "GRACE", "GRADE", "GRAIN", "GRAND", "GRANT",
        "GRAPE", "GRAPH", "GRASP", "GRASS", "GRAVE", "GRAVY", "GRAZE", "GREAT", "GREED", "GREEN",
        "GREET", "GRIEF", "GRILL", "GRIND", "GRIPS", "GROAN", "GROOM", "GROSS", "GROUP", "GROVE",
        "GROWN", "GROWS", "GRUEL", "GRUNT", "GUARD", "GUESS", "GUEST", "GUIDE", "GUILD", "GUILT",
        "GUISE", "GULFS", "GULLY", "GUMMY", "GUNNY", "GURUS", "GUSTO", "GUSTY", "HABIT", "HAIRY",
        "HALLS", "HALTS", "HANDS", "HANDY", "HANGS", "HAPPY", "HARDY", "HARMS", "HARSH", "HASTE",
        "HASTY", "HATCH", "HAUNT", "HAVEN", "HAVOC", "HAWKS", "HAZEL", "HEADS", "HEALS", "HEAPS",
        "HEARD", "HEART", "HEATH", "HEATS", "HEAVY", "HEDGE", "HEELS", "HEFTY", "HEIRS", "HELIX",
        "HELLO", "HELPS", "HENCE", "HERON", "HIDES", "HIGHS", "HIKER", "HILLS", "HILLY", "HINGE",
        "HINTS", "HIPPO", "HIRES", "HOBBY", "HOIST", "HOLDS", "HOLES", "HOLLY", "HOMES", "HONED",
        "HONEY", "HONOR", "HOODS", "HOOKS", "HOOPS", "HOPES", "HORNS", "HORSE", "HOSTS", "HOTEL",
        "HOUND", "HOUSE", "HOVEL", "HOVER", "HOWLS", "HUMAN", "HUMID", "HUMOR", "HUNTS", "HURRY",
        "HURTS", "HUSKY", "HYENA", "ICONS", "IDEAL", "IDEAS", "IDIOM", "IDIOT", "IDLES", "IDOLS",
        "IMAGE", "IMPLY", "INDEX", "INDIE", "INNER", "INPUT", "INTRO", "IRONY", "ISLAM", "ISSUE",
        "ITEMS", "IVORY", "JAILS", "JEANS", "JELLY", "JERKS", "JEWEL", "JIFFY", "JOINT", "JOKES",
        "JOKER", "JOLLY", "JOUST", "JUDGE", "JUICE", "JUICY", "JUMBO", "JUMPS", "JUMPY", "JUNTA",
        "JUROR", "KEEPS", "KICKS", "KILLS", "KILTS", "KINDS", "KINGS", "KIOSK", "KITTY", "KNACK",
        "KNEAD", "KNEES", "KNELT", "KNIFE", "KNITS", "KNOBS", "KNOCK", "KNOTS", "KNOWN", "KNOWS",
        "LABEL", "LABOR", "LACED", "LACKS", "LADEN", "LADLE", "LAGER", "LAKES", "LAMPS", "LANCE",
        "LANDS", "LANES", "LAPSE", "LARGE", "LARVA", "LASER", "LATCH", "LATER", "LATEX", "LATHE",
        "LAUGH", "LAWNS", "LAYER", "LAYUP", "LEACH", "LEADS", "LEAFY", "LEAKS", "LEAKY", "LEANS",
        "LEAPS", "LEARN", "LEASE", "LEASH", "LEAST", "LEAVE", "LEDGE", "LEECH", "LEEKS", "LEGAL",
        "LEMON", "LENDS", "LEPER", "LEVEL", "LEVER", "LIBEL", "LIENS", "LIFTS", "LIGHT", "LIKED",
        "LIKEN", "LILAC", "LIMBO", "LIMIT", "LINED", "LINEN", "LINER", "LINES", "LINGO", "LINKS",
        "LIONS", "LIPID", "LISTS", "LITER", "LITHE", "LIVER", "LIVES", "LIVID", "LLAMA", "LOADS",
        "LOAFS", "LOAMS", "LOANS", "LOATH", "LOBBY", "LOCAL", "LOCKS", "LOCUS", "LODGE", "LOFTY",
        "LOGIC", "LOINS", "LONER", "LOOSE", "LORDS", "LOSER", "LOSES", "LOUSY", "LOVED", "LOVER",
        "LOVES", "LOWER", "LOWLY", "LOYAL", "LUCID", "LUCKY", "LUMEN", "LUMPS", "LUMPY", "LUNAR",
        "LUNCH", "LUNGE", "LUNGS", "LURCH", "LURED", "LURID", "LURKS", "LYING", "LYMPH", "LYNCH",
        "LYRIC", "MACRO", "MADAM", "MADLY", "MAFIA", "MAGIC", "MAGMA", "MAIDS", "MAILS", "MAIMS",
        "MAINS", "MAJOR", "MAKER", "MAKES", "MALES", "MALLS", "MALTS", "MAMMA", "MANOR", "MAPLE",
        "MARCH", "MARES", "MARKS", "MARRY", "MARSH", "MASKS", "MASON", "MATCH", "MATES", "MATHS",
        "MATTE", "MAUVE", "MAXIM", "MAYBE", "MAYOR", "MAZES", "MEALS", "MEANS", "MEANT", "MEATS",
        "MEATY", "MECCA", "MEDAL", "MEDIA", "MEDIC", "MEETS", "MELON", "MELTS", "MEMOS", "MENDS",
        "MENUS", "MERCY", "MERGE", "MERIT", "MERRY", "MESSY", "METAL", "METER", "METRO", "MICRO",
        "MIDST", "MIGHT", "MILKS", "MILKY", "MILLS", "MIMIC", "MINCE", "MINDS", "MINED", "MINER",
        "MINES", "MINTY", "MINUS", "MIRTH", "MISER", "MISTY", "MITES", "MIXED", "MIXER", "MIXES",
        "MOANS", "MOATS", "MOCKS", "MODAL", "MODEL", "MODEM", "MODES", "MOIST", "MOLDS", "MOLDY",
        "MOLES", "MONEY", "MONKS", "MONTH", "MOODS", "MOODY", "MOONS", "MOORS", "MOOSE", "MORAL",
        "MORON", "MORPH", "MORSE", "MOSSY", "MOTEL", "MOTIF", "MOTOR", "MOTTO", "MOULD", "MOUND",
        "MOUNT", "MOURN", "MOUSE", "MOUSY", "MOUTH", "MOVED", "MOVER", "MOVES", "MOVIE", "MOWED",
        "MOWER", "MUCKY", "MUCUS", "MUDDY", "MUFFS", "MULCH", "MUMMY", "MUMPS", "MURAL", "MURKY",
        "MUSHY", "MUSIC", "MUSKY", "MUSTY", "MUTED", "MYRRH", "MYTHS", "NACHO", "NAIVE", "NAKED",
        "NAMED", "NAMES", "NANNY", "NAPPY", "NASTY", "NATAL", "NAVAL", "NAVEL", "NAZIS", "NECKS",
        "NEEDS", "NEEDY", "NEGRO", "NEIGH", "NERVE", "NERVY", "NESTS", "NEVER", "NEWLY", "NEWTS",
        "NICER", "NICHE", "NIECE", "NIFTY", "NIGHT", "NINJA", "NINTH", "NOBLE", "NOBLY", "NODES",
        "NOISE", "NOISY", "NOMAD", "NONCE", "NOOKS", "NOONS", "NOOSE", "NORMS", "NORTH", "NOSED",
        "NOSES", "NOTCH", "NOTED", "NOTES", "NOVEL", "NUBBY", "NUDIE", "NUDGE", "NULLS", "NUMBS",
        "NURSE", "NUTTY", "NYLON", "NYMPH", "OAKEN", "OASES", "OASIS", "OATHS", "OBESE", "OBEYS",
        "OCCUR", "OCEAN", "ODDLY", "ODORS", "OFFER", "OFTEN", "OILED", "OILER", "OLDER", "OLIVE",
        "OMEGA", "OMENS", "OMITS", "ONION", "ONSET", "OPENS", "OPERA", "OPIUM", "OPTIC", "ORBIT",
        "ORDER", "ORGAN", "OTHER", "OTTER", "OUGHT", "OUNCE", "OUTDO", "OUTER", "OUTGO", "OVALS",
        "OVARY", "OVATE", "OVENS", "OVERT", "OWING", "OWNED", "OWNER", "OXIDE", "OZONE", "PACED",
        "PACER", "PACES", "PACKS", "PACTS", "PADDY", "PAGAN", "PAGED", "PAGER", "PAGES", "PAILS",
        "PAINS", "PAINT", "PAIRS", "PALMS", "PALSY", "PANEL", "PANGS", "PANIC", "PANSY", "PANTS",
        "PAPAW", "PAPAL", "PAPER", "PAPPY", "PARKS", "PARSE", "PARTS", "PARTY", "PASSE", "PASTA",
        "PASTE", "PASTY", "PATCH", "PATIO", "PATSY", "PATTY", "PAUSE", "PAVED", "PAVES", "PAWED",
        "PAWNS", "PAYEE", "PAYER", "PEACE", "PEACH", "PEAKS", "PEAKY", "PEALS", "PEARL", "PEARS",
        "PEATS", "PECAN", "PECKS", "PEDAL", "PEEKS", "PEELS", "PEERS", "PEEVE", "PENAL", "PENCE",
        "PENIS", "PENNY", "PERCH", "PERKS", "PERKY", "PESOS", "PESTS", "PETAL", "PETTY", "PHASE",
        "PHONE", "PHONY", "PHOTO", "PIANO", "PICKY", "PIECE", "PIETY", "PIGGY", "PIKES", "PILED",
        "PILES", "PILLS", "PILOT", "PIMPS", "PINCH", "PINES", "PINGS", "PINKY", "PINTS", "PIOUS",
        "PIPED", "PIPER", "PIPES", "PIQUE", "PITCH", "PITHY", "PIVOT", "PIXEL", "PIXIE", "PIZZA",
        "PLACE", "PLAID", "PLAIN", "PLANE", "PLANK", "PLANS", "PLANT", "PLATE", "PLATO", "PLAYS",
        "PLAZA", "PLEAD", "PLEAS", "PLEAT", "PLEBS", "PLIED", "PLIER", "PLIES", "PLOTS", "PLOWS",
        "PLOYS", "PLUCK", "PLUGS", "PLUMB", "PLUME", "PLUMP", "PLUMS", "PLUNK", "PLUSH", "POACH",
        "POCKS", "POEMS", "POESY", "POETS", "POINT", "POISE", "POKED", "POKER", "POKES", "POLAR",
        "POLED", "POLES", "POLIO", "POLKA", "POLLS", "POLYP", "PONDS", "POOLS", "POPES", "POPPY",
        "POPUP", "PORCH", "PORED", "PORES", "PORGY", "PORNO", "PORTS", "POSED", "POSER", "POSES",
        "POSIT", "POSSE", "POSTS", "POTTY", "POUCH", "POULT", "POUND", "POURS", "POUTS", "POWER",
        "PRAMS", "PRANK", "PRATE", "PRAWN", "PRAYS", "PREPS", "PRESS", "PRICE", "PRICK", "PRIDE",
        "PRIED", "PRIME", "PRIMO", "PRIMP", "PRIMS", "PRINT", "PRIOR", "PRISM", "PRIVY", "PRIZE",
        "PROBE", "PRODS", "PROFS", "PROMO", "PROMS", "PRONE", "PRONG", "PROOF", "PROPS", "PROSE",
        "PROSY", "PROUD", "PROVE", "PROWL", "PROWS", "PROXY", "PRUDE", "PRUNE", "PSALM", "PUBES",
        "PUBIC", "PUCKS", "PUDGY", "PUFFS", "PUFFY", "PULLS", "PULPS", "PULPY", "PULSE", "PUMAS",
        "PUMPS", "PUNCH", "PUNKS", "PUNNY", "PUNTS", "PUPIL", "PUPPY", "PUREE", "PURER", "PURGE",
        "PURRS", "PURSE", "PUSHY", "PUSSY", "PUTTY", "PYGMY", "QUACK", "QUADS", "QUAFF", "QUAIL",
        "QUAKE", "QUALM", "QUARK", "QUART", "QUASH", "QUASI", "QUAYS", "QUEEN", "QUEER", "QUELL",
        "QUERY", "QUEST", "QUEUE", "QUICK", "QUIET", "QUIFF", "QUILL", "QUILT", "QUIPS", "QUIRK",
        "QUITE", "QUITS", "QUOTA", "QUOTE", "QUOTH", "RABBI", "RABID", "RACED", "RACER", "RACES",
        "RACKS", "RADAR", "RADII", "RADIO", "RADON", "RAFTS", "RAGED", "RAGES", "RAIDS", "RAILS",
        "RAINS", "RAINY", "RAISE", "RAITA", "RAKED", "RAKES", "RALLY", "RALPH", "RAMEN", "RAMPS",
        "RANCH", "RANDS", "RANDY", "RANGE", "RANGY", "RANKS", "RANTS", "RAPID", "RAPED", "RAPER",
        "RAPES", "RARER", "RASPY", "RATED", "RATER", "RATES", "RATIO", "RATTY", "RAVED", "RAVEL",
        "RAVEN", "RAVES", "RAWER", "RAYON", "RAZED", "RAZER", "RAZES", "RAZOR", "REACH", "REACT",
        "READS", "READY", "REALM", "REAMS", "REAPS", "REARM", "REARS", "REBUS", "REBUT", "RECAP",
        "RECUR", "RECUT", "REDID", "REDLY", "REEDS", "REEDY", "REEFS", "REEKS", "REEKY", "REELS",
        "REFER", "REFIT", "REGAL", "REHAB", "REIGN", "REINS", "RELAX", "RELAY", "RELIC", "REMIT",
        "REMIX", "RENAL", "RENEW", "RENTS", "REPAY", "REPEL", "REPLY", "RERUN", "RESET", "RESIN",
        "RETRO", "RETRY", "REUSE", "REVEL", "REVUE", "RHINO", "RHYME", "RIDER", "RIDES", "RIDGE",
        "RIFLE", "RIFTS", "RIGHT", "RIGID", "RIGOR", "RILED", "RILES", "RILLS", "RINDS", "RINGS",
        "RINKS", "RINSE", "RIOTS", "RIPEN", "RIPER", "RISEN", "RISER", "RISES", "RISKS", "RISKY",
        "RITES", "RITZY", "RIVAL", "RIVED", "RIVEN", "RIVER", "RIVET", "ROACH", "ROADS", "ROAMS",
        "ROARS", "ROAST", "ROBED", "ROBES", "ROBIN", "ROBOT", "ROCKS", "ROCKY", "RODEO", "ROGUE",
        "ROLES", "ROLLS", "ROMAN", "ROMPS", "ROOFS", "ROOKS", "ROOMS", "ROOMY", "ROOST", "ROOTS",
        "ROPED", "ROPER", "ROPES", "ROSES", "ROSIN", "ROTOR", "ROUGE", "ROUGH", "ROUND", "ROUSE",
        "ROUTE", "ROUTS", "ROVED", "ROVER", "ROVES", "ROWAN", "ROWDY", "ROWED", "ROWER", "ROYAL",
        "RUBLE", "RUDDY", "RUDER", "RUGBY", "RUINS", "RULED", "RULER", "RULES", "RUMBA", "RUMEN",
        "RUMOR", "RUMPS", "RUNES", "RUNGS", "RUNNY", "RUNTS", "RUNTY", "RURAL", "RUSES", "RUSHY",
        "RUSKS", "RUSTY", "SABER", "SABLE", "SACKS", "SADLY", "SAFER", "SAFES", "SAGAS", "SAGES",
        "SAGGY", "SAHIB", "SAILS", "SAINT", "SAITH", "SALAD", "SALES", "SALLY", "SALON", "SALSA",
        "SALTY", "SALVE", "SALVO", "SAMBA", "SANDY", "SANER", "SAPPY", "SARGE", "SARIS", "SASSY",
        "SATAN", "SATED", "SATIN", "SATYR", "SAUCE", "SAUCY", "SAUNA", "SAUTE", "SAVED", "SAVER",
        "SAVES", "SAVOR", "SAVOY", "SAVVY", "SAWED", "SAYER", "SCABS", "SCADS", "SCALE", "SCALP",
        "SCALY", "SCAMP", "SCAMS", "SCANS", "SCANT", "SCARE", "SCARF", "SCARY", "SCENE", "SCENT",
        "SCHWA", "SCION", "SCOFF", "SCOLD", "SCONE", "SCOOP", "SCOOT", "SCOPE", "SCORE", "SCORN",
        "SCOTS", "SCOUR", "SCOUT", "SCOWL", "SCRAM", "SCRAP", "SCREE", "SCREW", "SCRUB", "SCUBA",
        "SCUDS", "SCUFF", "SEALS", "SEAMS", "SEAMY", "SEARS", "SEATS", "SEDAN", "SEEDS", "SEEDY",
        "SEEKS", "SEEMS", "SEEPS", "SEERS", "SEGUE", "SEIZE", "SELLS", "SEMIS", "SENDS", "SENSE",
        "SEPIA", "SEPTA", "SERIF", "SERUM", "SERVE", "SETUP", "SEVEN", "SEVER", "SEWED", "SEWER",
        "SHACK", "SHADE", "SHADY", "SHAFT", "SHAKE", "SHAKY", "SHALE", "SHALL", "SHALT", "SHAME",
        "SHAMS", "SHANK", "SHAPE", "SHARD", "SHARE", "SHARK", "SHARP", "SHAVE", "SHAWL", "SHEAF",
        "SHEAR", "SHEDS", "SHEEN", "SHEEP", "SHEER", "SHEET", "SHEIK", "SHELF", "SHELL", "SHIED",
        "SHIFT", "SHILL", "SHIMS", "SHINE", "SHINY", "SHIPS", "SHIRE", "SHIRK", "SHIRT", "SHIVA",
        "SHOAL", "SHOCK", "SHOED", "SHOES", "SHONE", "SHOOK", "SHOOT", "SHOPS", "SHORE", "SHORN",
        "SHORT", "SHOTS", "SHOUT", "SHOVE", "SHOWN", "SHOWS", "SHOWY", "SHRED", "SHREW", "SHRUB",
        "SHRUG", "SHUCK", "SHUNS", "SHUNT", "SHUSH", "SHUTS", "SHYER", "SHYLY", "SIDED", "SIDES",
        "SIEGE", "SIEVE", "SIFTS", "SIGHS", "SIGHT", "SIGMA", "SIGNS", "SILKS", "SILKY", "SILLY",
        "SILOS", "SILTS", "SILTY", "SINCE", "SINEW", "SINGE", "SINGS", "SINKS", "SINUS", "SIRED",
        "SIREN", "SIRES", "SISSY", "SITAR", "SITED", "SITES", "SIXTH", "SIXTY", "SIZED", "SIZES",
        "SKATE", "SKEET", "SKEIN", "SKEWS", "SKIDS", "SKIED", "SKIER", "SKIES", "SKIFF", "SKILL",
        "SKIMP", "SKIMS", "SKINS", "SKINT", "SKIPS", "SKIRT", "SKITS", "SKULK", "SKULL", "SKUNK",
        "SLABS", "SLACK", "SLAGS", "SLAIN", "SLAKE", "SLAMS", "SLANG", "SLANT", "SLAPS", "SLASH",
        "SLATE", "SLATS", "SLAVE", "SLAYS", "SLEDS", "SLEEK", "SLEEP", "SLEET", "SLEPT", "SLICE",
        "SLICK", "SLIDE", "SLIER", "SLIME", "SLIMY", "SLING", "SLINK", "SLIPS", "SLITS", "SLOBS",
        "SLOGS", "SLOPE", "SLOPS", "SLOSH", "SLOTH", "SLOTS", "SLOWS", "SLUED", "SLUES", "SLUGS",
        "SLUMS", "SLUMP", "SLUNG", "SLUNK", "SLURP", "SLURS", "SLUSH", "SLYER", "SLYLY", "SMACK",
        "SMALL", "SMART", "SMASH", "SMEAR", "SMELL", "SMELT", "SMILE", "SMIRK", "SMITE", "SMITH",
        "SMOCK", "SMOKE", "SMOKY", "SMOTE", "SNACK", "SNAGS", "SNAIL", "SNAKE", "SNAKY", "SNAPS",
        "SNARE", "SNARL", "SNEAK", "SNEER", "SNIDE", "SNIFF", "SNIPE", "SNIPS", "SNITS", "SNOBS",
        "SNOOD", "SNOOP", "SNOOT", "SNORE", "SNORT", "SNOTS", "SNOUT", "SNOWS", "SNOWY", "SNUBS",
        "SNUCK", "SNUFF", "SNUGS", "SOAPY", "SOAPS", "SOARS", "SOBER", "SOCKS", "SODAS", "SOFAS",
        "SOFTY", "SOGGY", "SOILS", "SOLAR", "SOLED", "SOLES", "SOLID", "SOLOS", "SOLVE", "SONAR",
        "SONGS", "SONIC", "SONNY", "SOOTH", "SOOTY", "SOPHS", "SOPPY", "SORRY", "SORTS", "SOULS",
        "SOUND", "SOUPS", "SOUPY", "SOURS", "SOUTH", "SOWED", "SOWER", "SPACE", "SPADE", "SPANK",
        "SPANS", "SPARE", "SPARK", "SPARS", "SPASM", "SPATE", "SPAWN", "SPAYS", "SPEAK", "SPEAR",
        "SPECS", "SPEED", "SPELL", "SPEND", "SPENT", "SPERM", "SPEWS", "SPICE", "SPICY", "SPIED",
        "SPIEL", "SPIES", "SPIKE", "SPIKY", "SPILL", "SPILT", "SPINE", "SPINS", "SPINY", "SPIRE",
        "SPIRT", "SPITE", "SPITS", "SPLAT", "SPLAY", "SPLIT", "SPOIL", "SPOKE", "SPOOF", "SPOOK",
        "SPOOL", "SPOON", "SPORE", "SPORT", "SPOTS", "SPOUT", "SPRAY", "SPREE", "SPRIG", "SPUNK",
        "SPURN", "SPURS", "SPURT", "SQUAD", "SQUAT", "SQUAW", "SQUIB", "SQUID", "STABS", "STACK",
        "STAFF", "STAGE", "STAID", "STAIN", "STAIR", "STAKE", "STALE", "STALK", "STALL", "STAMP",
        "STAND", "STANK", "STAPH", "STARE", "STARK", "STARS", "START", "STASH", "STATE", "STATS",
        "STAVE", "STAYS", "STEAD", "STEAK", "STEAL", "STEAM", "STEED", "STEEL", "STEEP", "STEER",
        "STEMS", "STENO", "STEPS", "STERN", "STEWS", "STICK", "STIFF", "STILE", "STILL", "STILT",
        "STING", "STINK", "STINT", "STIRS", "STOAT", "STOCK", "STOIC", "STOKE", "STOLE", "STOMP",
        "STONE", "STONY", "STOOD", "STOOL", "STOOP", "STOPS", "STORE", "STORK", "STORM", "STORY",
        "STOUT", "STOVE", "STRAP", "STRAW", "STRAY", "STREP", "STRIP", "STROP", "STRUM", "STRUT",
        "STUBS", "STUCK", "STUDS", "STUDY", "STUFF", "STUMP", "STUNG", "STUNK", "STUNS", "STUNT",
        "STYLE", "STYLI", "SUAVE", "SUCHS", "SUCKS", "SUDAN", "SUDSY", "SUEDE", "SUGAR", "SUITS",
        "SUITE", "SULFA", "SULKS", "SULKY", "SULLY", "SUMAC", "SUNNY", "SUPER", "SURER", "SURGE",
        "SURLY", "SUSHI", "SWABS", "SWAMP", "SWAMY", "SWANK", "SWANS", "SWAPS", "SWARD", "SWARM",
        "SWASH", "SWATH", "SWATS", "SWAYS", "SWEAR", "SWEAT", "SWEEP", "SWEET", "SWELL", "SWEPT",
        "SWIFT", "SWIGS", "SWILL", "SWIMS", "SWINE", "SWING", "SWIPE", "SWIRL", "SWISH", "SWISS",
        "SWOON", "SWOOP", "SWORD", "SWORE", "SWORN", "SWUNG", "SYNOD", "SYRUP", "TABBY", "TABLE",
        "TABOO", "TACIT", "TACKS", "TACKY", "TACOS", "TACTS", "TAFFY", "TAILS", "TAINT", "TAKEN",
        "TAKER", "TAKES", "TALES", "TALKS", "TALKY", "TALLY", "TALON", "TAMED", "TAMER", "TAMES",
        "TANGO", "TANGY", "TANKS", "TANSY", "TAPED", "TAPER", "TAPES", "TAPIR", "TARDY", "TARED",
        "TARES", "TARNS", "TAROT", "TARPS", "TARRY", "TARTS", "TASKS", "TASTE", "TASTY", "TATTY",
        "TAUNT", "TAWNY", "TAXED", "TAXES", "TAXIS", "TEACH", "TEAKS", "TEALS", "TEAMS", "TEARS",
        "TEARY", "TEASE", "TEATS", "TEDDY", "TEENS", "TEENY", "TEETH", "TELLS", "TELLY", "TEMPS",
        "TEMPT", "TENDS", "TENET", "TENOR", "TENSE", "TENTH", "TENTS", "TEPEE", "TEPID", "TERMS",
        "TERNS", "TERRA", "TERRY", "TERSE", "TESTS", "TESTY", "TEXAS", "TEXTS", "THANK", "THAWS",
        "THEFT", "THEIR", "THEME", "THENS", "THERE", "THESE", "THETA", "THICK", "THIEF", "THIGH",
        "THING", "THINK", "THIRD", "THORN", "THOSE", "THREE", "THREW", "THROB", "THROW", "THRUM",
        "THUDS", "THUGS", "THUMB", "THUMP", "THUNK", "THYME", "TIARA", "TIBIA", "TICKS", "TIDAL",
        "TIDED", "TIDES", "TIERS", "TIGER", "TIGHT", "TIKES", "TILDE", "TILED", "TILES", "TILLS",
        "TILTS", "TIMED", "TIMER", "TIMES", "TIMID", "TINES", "TINGE", "TINGS", "TINNY", "TINTS",
        "TIPSY", "TIRED", "TIRES", "TITAN", "TITHE", "TITLE", "TIZZY", "TOADS", "TOAST", "TODAY",
        "TODDY", "TOFFY", "TOGAS", "TOILS", "TOKEN", "TOKED", "TOKES", "TOLLS", "TOMBS", "TOMES",
        "TONAL", "TONED", "TONER", "TONES", "TONGS", "TONIC", "TOOLS", "TOOTH", "TOOTS", "TOPAZ",
        "TOPIC", "TOQUE", "TORAH", "TORCH", "TORES", "TORSO", "TORUS", "TOTAL", "TOTEM", "TOUCH",
        "TOUGH", "TOURS", "TOUTS", "TOWED", "TOWEL", "TOWER", "TOWNS", "TOXIC", "TOXIN", "TOYED",
        "TRACE", "TRACK", "TRACT", "TRADE", "TRAIL", "TRAIN", "TRAIT", "TRAMP", "TRAMS", "TRANS",
        "TRAPS", "TRASH", "TRAWL", "TRAYS", "TREAD", "TREAT", "TREED", "TREES", "TREKS", "TREND",
        "TRESS", "TRIAD", "TRIAL", "TRIBE", "TRICE", "TRICK", "TRIED", "TRIER", "TRIES", "TRIKE",
        "TRILL", "TRIMS", "TRINE", "TRIOS", "TRIPE", "TRIPS", "TRITE", "TROLL", "TROMP", "TROOP",
        "TROPE", "TROTH", "TROTS", "TROUT", "TROVE", "TRUCE", "TRUCK", "TRUED", "TRUER", "TRUES",
        "TRULY", "TRUMP", "TRUNK", "TRUSS", "TRUST", "TRUTH", "TRYST", "TSARS", "TUBAS", "TUBBY",
        "TUBED", "TUBER", "TUBES", "TUCKS", "TUFTS", "TULIP", "TULLE", "TULSA", "TUMID", "TUMMY",
        "TUMOR", "TUNED", "TUNER", "TUNES", "TUNIC", "TURBO", "TURFS", "TURNS", "TURPS", "TUSKS",
        "TUTOR", "TUTUS", "TUXES", "TWAIN", "TWANG", "TWEAK", "TWEED", "TWEET", "TWERP", "TWICE",
        "TWIGS", "TWILL", "TWINE", "TWINS", "TWINY", "TWIRL", "TWIST", "TWITS", "TYING", "TYKES",
        "TYPED", "TYPES", "TYPIC", "TYPOS", "UDDER", "UKASE", "ULCER", "ULTRA", "UMBER", "UMPED",
        "UNCLE", "UNCUT", "UNDER", "UNDID", "UNDUE", "UNFED", "UNFIT", "UNIFY", "UNION", "UNITE",
        "UNITS", "UNITY", "UNLIT", "UNMET", "UNPEG", "UNSAY", "UNSET", "UNTIE", "UNTIL", "UNWED",
        "UNZIP", "UPEND", "UPPER", "UPSET", "URBAN", "URGED", "URGES", "URINE", "USAGE", "USHER",
        "USING", "USUAL", "USURP", "USURY", "UTERO", "UTTER", "VACUA", "VAGUE", "VAINLY", "VALET",
        "VALID", "VALOR", "VALUE", "VALVE", "VAMPS", "VANES", "VAPOR", "VASES", "VASTS", "VAULT",
        "VAUNT", "VEALS", "VEERS", "VEGAN", "VEINS", "VEINY", "VELAR", "VELDS", "VELUM", "VENAL",
        "VENDS", "VENOM", "VENTS", "VENUE", "VENUS", "VERBS", "VERGE", "VERSE", "VERSO", "VERVE",
        "VESTS", "VETCH", "VEXED", "VEXES", "VIALS", "VIBES", "VICAR", "VIDEO", "VIEWS", "VIGIL",
        "VIGOR", "VILLA", "VINES", "VINYL", "VIOLA", "VIPER", "VIRAL", "VIRUS", "VISAS", "VISIT",
        "VISOR", "VISTA", "VITAL", "VIVID", "VIXEN", "VOCAL", "VODKA", "VOGUE", "VOICE", "VOIDS",
        "VOILA", "VOLES", "VOLTS", "VOMIT", "VOTED", "VOTER", "VOTES", "VOUCH", "VOWED", "VOWEL",
        "VYING", "WACKY", "WADED", "WADER", "WADES", "WAFER", "WAFTS", "WAGED", "WAGER", "WAGES",
        "WAGON", "WAIFS", "WAILS", "WAIST", "WAITS", "WAIVE", "WAKED", "WAKEN", "WAKER", "WAKES",
        "WALED", "WALES", "WALKS", "WALLS", "WALTZ", "WANDS", "WANED", "WANES", "WANTS", "WARDS",
        "WARED", "WARES", "WARMS", "WARNS", "WARPS", "WARTS", "WASHY", "WASPS", "WASTE", "WATCH",
        "WATER", "WAVED", "WAVER", "WAVES", "WAXED", "WAXEN", "WAXES", "WEALD", "WEANS", "WEARS",
        "WEARY", "WEAVE", "WEBBY", "WEBER", "WEDGE", "WEEDS", "WEEDY", "WEEKS", "WEEPY", "WEIGH",
        "WEIRD", "WELDS", "WELLS", "WELSH", "WELTS", "WENCH", "WENDS", "WHACK", "WHALE", "WHARF",
        "WHEAT", "WHEEL", "WHELK", "WHELP", "WHENS", "WHERE", "WHETS", "WHICH", "WHIFF", "WHILE",
        "WHIMS", "WHINE", "WHINY", "WHIPS", "WHIRL", "WHIRR", "WHIRS", "WHISK", "WHITE", "WHITS",
        "WHOLE", "WHOMP", "WHOOP", "WHOPS", "WHORE", "WHORL", "WHOSE", "WICKS", "WIDEN", "WIDER",
        "WIDOW", "WIDTH", "WIELD", "WIFED", "WIFES", "WIFEY", "WIGHT", "WILDS", "WILED", "WILES",
        "WILLS", "WILLY", "WILTS", "WIMPS", "WIMPY", "WINCE", "WINCH", "WINDS", "WINDY", "WINED",
        "WINES", "WINGS", "WINKS", "WIPED", "WIPER", "WIPES", "WIRED", "WIRES", "WISER", "WISES",
        "WISPS", "WISPY", "WITCH", "WITTY", "WIVED", "WIVES", "WIZEN", "WOADS", "WOKEN", "WOLDS",
        "WOMAN", "WOMBS", "WOMEN", "WONKY", "WOODS", "WOODY", "WOOED", "WOOER", "WOOFS", "WOOLS",
        "WOOLY", "WOOZY", "WORDY", "WORDS", "WORKS", "WORLD", "WORMS", "WORMY", "WORRY", "WORSE",
        "WORST", "WORTH", "WOULD", "WOUND", "WOVEN", "WOWED", "WRACK", "WRATH", "WREAK", "WRECK",
        "WRENS", "WREST", "WRIER", "WRING", "WRIST", "WRITE", "WRITS", "WRONG", "WROTE", "WRUNG",
        "WRYER", "WRYLY", "XENON", "XEROX", "YACHT", "YAHOO", "YANKS", "YARDS", "YARNS", "YAWLS",
        "YAWNS", "YEAHS", "YEARS", "YEAST", "YELLS", "YIELD", "YODEL", "YOGIS", "YOKED", "YOKEL",
        "YOKES", "YOLKS", "YOUNG", "YOURS", "YOUTH", "YOWLS", "YUCCA", "YUCKY", "YULES", "YUMMY",
        "ZEALS", "ZEBRA", "ZEROS", "ZESTS", "ZESTY", "ZILCH", "ZINCS", "ZINES", "ZINGS", "ZINGY",
        "ZIPPY", "ZONED", "ZONES", "ZOOMS"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get game mode from intent
        val gameModeString = intent.getStringExtra("GAME_MODE")
        val gameMode = if (gameModeString != null) {
            try {
                MainActivity.GameMode.valueOf(gameModeString)
            } catch (e: IllegalArgumentException) {
                MainActivity.GameMode.BOT
            }
        } else {
            MainActivity.GameMode.BOT
        }

        words = loadWords() // Load/prepare the word list

        setupUI()          // Creates game board and keyboard visuals
        initializeGame()   // Sets up initial game state (picks word, resets attempts etc.)
        setupClickListeners() // Attaches listeners to buttons
    }

    private fun loadWords(): List<String> {
        // For now, using the hardcoded list.
        // Later, you could load from assets:
        // return assets.open("words.txt").bufferedReader().readLines().map { it.uppercase() }
        if (defaultWordList.isEmpty()) {
            // Fallback if the list is somehow empty, though it's hardcoded here
            return listOf("ERROR", "WORDS", "EMPTY")
        }
        return defaultWordList.filter { it.length == WORD_LENGTH } // Ensure all words are of correct length
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP) // Retained from your original code
    private fun setupUI() {
        supportActionBar?.hide() // Retained from your original code
        // Consider theming for status bar color if using modern themes, or keep explicit:
        // window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent) // Example

        createGameBoard()
        createKeyboard()
    }

    private fun createGameBoard() {
        gameBoard = Array(MAX_ATTEMPTS) { row ->
            Array(WORD_LENGTH) { col ->
                TextView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0 // Use weights for distribution
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4)) // Use dp for margins
                    }
                    setTextColor(ContextCompat.getColor(context, R.color.white)) // Ensure R.color.white exists
                    textSize = 24f
                    gravity = Gravity.CENTER
                    //setBackgroundResource(R.drawable.ic_launcher) // Initial placeholder, will be game_cell_default
                    // Set a default background for empty cells
                    setBackgroundResource(R.drawable.game_cell_default) // **ACTION: Create this drawable**
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

        keyRows.forEach { rowString ->
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    val marginVertical = dpToPx(2)
                    setMargins(0, marginVertical, 0, marginVertical)
                }
            }

            rowString.forEach { char ->
                val keyButton = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                    text = char.toString()
                    val keyLayoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                    ).apply {
                        val marginHorizontal = dpToPx(2)
                        setMargins(marginHorizontal, 0, marginHorizontal, 0)
                    }
                    layoutParams = keyLayoutParams

                    setBackgroundColor(ContextCompat.getColor(context, R.color.keyboard_key_background))
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 16f
                    setPadding(0, dpToPx(12), 0, dpToPx(12))

                    setOnClickListener { onKeyPress(char) }
                }
                keyboardButtons[char] = keyButton
                rowLayout.addView(keyButton)
            }
            binding.keyboardContainer.addView(rowLayout)
        }

        // Add bottom row with Backspace and Submit buttons
        val bottomRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                val marginVertical = dpToPx(2)
                setMargins(0, marginVertical, 0, marginVertical)
            }
        }

        // Backspace button
        val backspaceButton = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
            text = "⌫"
            val backspaceLayoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.5f
            ).apply {
                val marginHorizontal = dpToPx(2)
                setMargins(marginHorizontal, 0, marginHorizontal, 0)
            }
            layoutParams = backspaceLayoutParams
            setBackgroundColor(ContextCompat.getColor(context, R.color.keyboard_key_background))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 18f
            setPadding(0, dpToPx(12), 0, dpToPx(12))
            setOnClickListener { onBackspace() }
        }
        bottomRow.addView(backspaceButton)

        // Submit button
        val submitButton = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
            text = getString(R.string.submit)
            val submitLayoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                2.5f
            ).apply {
                val marginHorizontal = dpToPx(2)
                setMargins(marginHorizontal, 0, marginHorizontal, 0)
            }
            layoutParams = submitLayoutParams
            setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 16f
            setPadding(0, dpToPx(12), 0, dpToPx(12))
            setOnClickListener { submitGuess() }
        }
        bottomRow.addView(submitButton)

        binding.keyboardContainer.addView(bottomRow)
    }


    private fun initializeGame() {
        if (words.isEmpty()) {
            showMessage("Error: Word list is empty. Cannot start game.", Snackbar.LENGTH_INDEFINITE)
            gameOver = true // Prevent further interaction
            return
        }

        currentWord = words.random().uppercase() // Ensure currentWord is also uppercase
        Log.d("GameActivity", "Current word: $currentWord") // For debugging
        currentGuess = ""
        attempts = 0
        gameWon = false
        gameOver = false
        usedWords.clear()

        clearGameBoardDisplay()
        resetKeyboardColors()
        updateGuessCounter()

        binding.btnPlayAgain.visibility = View.GONE

        // showMessage("New game started!", Snackbar.LENGTH_SHORT) // Optional: message to user
    }

    private fun setupClickListeners() {
        // Keyboard keys are set up in createKeyboard
        binding.btnPlayAgain.setOnClickListener { startNewGame() }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun onKeyPress(char: Char) {
        if (gameOver || currentGuess.length >= WORD_LENGTH) return

        currentGuess += char.uppercaseChar() // Store guess in uppercase
        updateGameBoardDisplay() // Show the typed character
    }

    private fun onBackspace() {
        if (currentGuess.isNotEmpty() && !gameOver) {
            currentGuess = currentGuess.dropLast(1)
            updateGameBoardDisplay() // Update the display to remove the char
        }
    }

    private fun submitGuess() {
        if (gameOver) return
        if (currentGuess.length != WORD_LENGTH) {
            showMessage("Word must be $WORD_LENGTH letters long", Snackbar.LENGTH_SHORT)
            return
        }

        // Word validation (check against the loaded 'words' list)
        if (!words.contains(currentGuess)) { // currentGuess is already uppercase
            showMessage("Not in word list", Snackbar.LENGTH_SHORT)
            // currentGuess = "" // Optionally clear invalid guess
            // updateGameBoardDisplay()
            return
        }

        if (usedWords.contains(currentGuess)) {
            showMessage("Word already guessed", Snackbar.LENGTH_SHORT)
            return
        }
        usedWords.add(currentGuess)

        val feedback = generateFeedback(currentGuess, currentWord)
        applyFeedbackToBoard(attempts, currentGuess, feedback)

        updateKeyboardColors(currentGuess, feedback)

        if (currentGuess == currentWord) {
            gameWon = true
            gameOver = true
            showMessage("Congratulations! You guessed it: $currentWord", Snackbar.LENGTH_INDEFINITE)
            binding.btnPlayAgain.visibility = View.VISIBLE
        } else {
            attempts++
            if (attempts >= MAX_ATTEMPTS) {
                gameOver = true
                showMessage("Game Over. The word was: $currentWord", Snackbar.LENGTH_INDEFINITE)
                binding.btnPlayAgain.visibility = View.VISIBLE
            } else {
                // showMessage("Try again!", Snackbar.LENGTH_SHORT) // Optional feedback
            }
        }
        currentGuess = "" // Clear for next guess, AFTER processing current one
        // updateGameBoardDisplay() // Call this if you want the next input row to immediately clear (but it's handled by next key press)
        updateGuessCounter()
    }


    private fun generateFeedback(guess: String, actualWord: String): List<CharFeedback> {
        val feedback = MutableList(WORD_LENGTH) { CharFeedback.NOT_IN_WORD }
        val actualWordCharCounts = actualWord.groupingBy { it }.eachCount().toMutableMap()

        // First pass: Correct position (Green)
        for (i in guess.indices) {
            if (guess[i] == actualWord[i]) {
                feedback[i] = CharFeedback.CORRECT_POSITION
                actualWordCharCounts[actualWord[i]] = (actualWordCharCounts[actualWord[i]] ?: 0) - 1
            }
        }

        // Second pass: Wrong position (Yellow)
        for (i in guess.indices) {
            if (feedback[i] == CharFeedback.NOT_IN_WORD) { // Only check if not already green
                if (actualWord.contains(guess[i]) && (actualWordCharCounts[guess[i]] ?: 0) > 0) {
                    feedback[i] = CharFeedback.WRONG_POSITION
                    actualWordCharCounts[guess[i]] = (actualWordCharCounts[guess[i]] ?: 0) - 1
                }
            }
        }
        return feedback
    }

    private fun applyFeedbackToBoard(attemptRow: Int, guess: String, feedback: List<CharFeedback>) {
        if (attemptRow >= MAX_ATTEMPTS) return

        for (i in guess.indices) {
            val cell = gameBoard[attemptRow][i]
            cell.text = guess[i].toString() // Already uppercase
            when (feedback[i]) {
                CharFeedback.CORRECT_POSITION -> cell.setBackgroundResource(R.drawable.cell_correct_position)
                CharFeedback.WRONG_POSITION -> cell.setBackgroundResource(R.drawable.game_cell_present)
                CharFeedback.NOT_IN_WORD -> cell.setBackgroundResource(R.drawable.game_cell_absent)
            }
        }
    }

    // Track keyboard key states
    private val keyboardKeyStates = mutableMapOf<Char, CharFeedback>()

    private fun updateKeyboardColors(guess: String, feedback: List<CharFeedback>) {
        for (i in guess.indices) {
            val char = guess[i]
            val button = keyboardButtons[char]
            val newCharFeedback = feedback[i]

            button?.let {
                val currentState = keyboardKeyStates[char]

                // Only upgrade the key color: Green > Yellow > Gray
                when {
                    newCharFeedback == CharFeedback.CORRECT_POSITION -> {
                        keyboardKeyStates[char] = CharFeedback.CORRECT_POSITION
                        it.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                    }
                    newCharFeedback == CharFeedback.WRONG_POSITION && 
                        currentState != CharFeedback.CORRECT_POSITION -> {
                        keyboardKeyStates[char] = CharFeedback.WRONG_POSITION
                        it.setBackgroundColor(ContextCompat.getColor(this, R.color.present))
                    }
                    newCharFeedback == CharFeedback.NOT_IN_WORD && 
                        currentState == null -> {
                        keyboardKeyStates[char] = CharFeedback.NOT_IN_WORD
                        it.setBackgroundColor(ContextCompat.getColor(this, R.color.absent))
                    }
                }
            }
        }
    }


    private fun clearGameBoardDisplay() {
        for (rowIndex in gameBoard.indices) {
            for (colIndex in gameBoard[rowIndex].indices) {
                val cell = gameBoard[rowIndex][colIndex]
                cell.text = ""
                cell.setBackgroundResource(R.drawable.game_cell_default) // **ACTION: Create this drawable**
            }
        }
    }

    private fun resetKeyboardColors() {
        keyboardButtons.forEach { (_, button) ->
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.keyboard_key_background))
        }
        keyboardKeyStates.clear()
    }

    private fun updateGuessCounter() {
        // Update the header guess counter TextView
        binding.guessCounter.text = "Guess ${attempts + 1}/$MAX_ATTEMPTS"
    }

    private fun updateGameBoardDisplay() {
        // Display current guess in the active row
        val currentRow = attempts
        if (currentRow < MAX_ATTEMPTS) {
            for (i in 0 until WORD_LENGTH) {
                val cell = gameBoard[currentRow][i]
                if (i < currentGuess.length) {
                    cell.text = currentGuess[i].toString() // currentGuess is already uppercase
                } else {
                    cell.text = ""
                }
                // Only reset background for current input row if it was previously colored by feedback (unlikely here)
                // cell.setBackgroundResource(R.drawable.game_cell_default)
            }
        }
    }

    private fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(binding.root, message, duration).show()
    }

    private fun startNewGame() {
        initializeGame()
    }

    // Helper for dp to pixel conversion
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
