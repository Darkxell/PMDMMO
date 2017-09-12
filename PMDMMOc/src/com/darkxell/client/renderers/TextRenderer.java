package com.darkxell.client.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.client.resources.Res;
import com.darkxell.common.util.Message;

public class TextRenderer
{

	public static enum PMDChar
	{
		new_line("<br>", 0, 0),
		space(" ", 0, 4),
		a("a", 1, 5),
		b("b", 2, 5),
		c("c", 3, 5),
		d("d", 4, 5),
		e("e", 5, 5),
		f("f", 6, 5),
		g("g", 7, 5),
		h("h", 8, 5),
		i("i", 9, 3),
		j("j", 10, 3),
		k("k", 11, 5),
		l("l", 12, 3),
		m("m", 13, 8),
		n("n", 14, 5),
		o("o", 15, 5),
		p("p", 16, 5),
		q("q", 17, 5),
		r("r", 18, 5),
		s("s", 19, 5),
		t("t", 20, 4),
		u("u", 21, 5),
		v("v", 22, 6),
		w("w", 23, 8),
		x("x", 24, 6),
		y("y", 25, 5),
		z("z", 26, 5),
		A("A", 27, 6),
		B("B", 28, 6),
		C("C", 29, 6),
		D("D", 30, 6),
		E("E", 31, 5),
		F("F", 32, 5),
		G("G", 33, 6),
		H("H", 34, 6),
		I("I", 35, 4),
		J("J", 36, 6),
		K("K", 37, 6),
		L("L", 38, 5),
		M("M", 39, 8),
		N("N", 40, 6),
		O("O", 41, 6),
		P("P", 42, 6),
		Q("Q", 43, 6),
		R("R", 44, 6),
		S("S", 45, 6),
		T("T", 46, 6),
		U("U", 47, 6),
		V("V", 48, 6),
		W("W", 49, 10),
		X("X", 50, 6),
		Y("Y", 51, 6),
		Z("Z", 52, 6),
		num1("1", 53, 6),
		num2("2", 54, 6),
		num3("3", 55, 6),
		num4("4", 56, 6),
		num5("5", 57, 6),
		num6("6", 58, 6),
		num7("7", 59, 6),
		num8("8", 60, 6),
		num9("9", 61, 6),
		num0("0", 62, 6),
		column(":", 63, 2),
		plus("+", 64, 6),
		minus("-", 65, 5),
		coma(",", 66, 3),
		dot(".", 67, 3),
		exclamation_("<!>", 68, 4),
		exclamation("!", 69, 4),
		interrogation_("<?>", 70, 6),
		interrogation("?", 71, 6),
		apostrophe_("`", 72, 3),
		apostrophe("'", 73, 3),
		quote_("<\">", 74, 6),
		quote("\"", 75, 6),
		male("<male>", 76, 8),
		female("<female>", 77, 6),
		space_visible("_", 78, 10),
		three_dots("<dots>", 79, 9),
		parenthesis_c(")", 80, 5),
		parenthesis_o("(", 81, 5),
		slash("/", 82, 5),
		poke1("<poke1>", 83, 8),
		poke2("<poke2>", 84, 7),
		e_accent("é", 85, 7),
		tilde("~", 86, 6),
		sharp("#", 87, 9),
		music("<music>", 88, 8),
		tick("<tick>", 89, 9),
		star("<star>", 90, 7),
		glued("<glued>", 91, 8),
		mission("<mission>", 92, 8),
		mission_accepted("<mission-a>", 93, 10),
		news("<news>", 94, 10),
		story("<story>", 95, 9),
		speech_bubble("<speech>", 96, 9),
		key_A("<key-a>", 97, 9),
		key_B("<key-b>", 98, 10),
		key_L("<key-l>", 99, 10),
		key_R("<key-r>", 100, 10),
		key_PLUS("<key-+>", 101, 10),
		key_SELECT1("<select1>", 102, 10),
		key_SELECT2("<select2>", 103, 10),
		buy_1("<1>", 104, 6),
		buy_2("<2>", 105, 6),
		buy_3("<3>", 106, 6),
		buy_4("<4>", 107, 6),
		buy_5("<5>", 108, 6),
		buy_6("<6>", 109, 6),
		buy_7("<7>", 110, 6),
		buy_8("<8>", 111, 6),
		buy_9("<9>", 112, 6),
		buy_0("<0>", 113, 6),
		orb("<orb>", 114, 10),
		tm_used("<tmu>", 120, 10),
		hm("<hm>", 121, 10),
		tm_0("<tm0>", 122, 10),
		tm_1("<tm1>", 123, 10),
		tm_2("<tm2>", 124, 10),
		tm_3("<tm3>", 125, 10),
		tm_4("<tm4>", 126, 10),
		tm_5("<tm5>", 127, 10),
		tm_6("<tm6>", 128, 10),
		tm_7("<tm7>", 129, 10),
		tm_8("<tm8>", 130, 10),
		tm_9("<tm9>", 131, 10),
		tm_10("<tm10>", 132, 10),
		tm_11("<tm11>", 133, 10),
		tm_12("<tm12>", 134, 10),
		tm_13("<tm13>", 135, 10),
		tm_14("<tm14>", 136, 10),
		tm_15("<tm15>", 137, 10),
		tm_16("<tm16>", 138, 10),
		tm_17("<tm17>", 139, 10);

		public static PMDChar find(String value)
		{
			for (PMDChar c : values())
				if (c.value.equals(value)) return c;
			return null;
		}

		/** Location on the Font sprite. */
		public final int id;
		public final String value;

		/** Width of the sprite. */
		public final int width;

		private PMDChar(String value, int id, int width)
		{
			this.value = value;
			this.id = id;
			this.width = width;
		}

	}

	public static final int CHAR_HEIGHT = 10;
	private static final int GRID_COLS = 20;
	private static final int GRID_WIDTH = CHAR_HEIGHT, GRID_HEIGHT = CHAR_HEIGHT;
	public static final int LINE_SPACING = 3;
	public static final TextRenderer instance = new TextRenderer();

	/** Called on startup to load the font. */
	public static void load()
	{}

	private HashMap<PMDChar, BufferedImage> sprites;

	private TextRenderer()
	{
		this.sprites = new HashMap<TextRenderer.PMDChar, BufferedImage>();
		BufferedImage source = Res.getBase("resources/hud/font.png");
		for (PMDChar c : PMDChar.values())
			this.sprites.put(c,
					Res.createimage(source, c.id % GRID_COLS * GRID_WIDTH, (c.id - c.id % GRID_COLS) / GRID_COLS * GRID_HEIGHT, GRID_WIDTH, GRID_HEIGHT));
	}

	public ArrayList<PMDChar> decode(String text)
	{
		ArrayList<PMDChar> chars = new ArrayList<TextRenderer.PMDChar>();
		if (text == null) return chars;
		int c = 0;
		String value;
		while (c < text.length())
		{
			if (text.charAt(c) == '<')
			{
				value = "";
				++c;
				while (c < text.length() && text.charAt(c) != '>')
				{
					value += text.charAt(c);
					++c;
				}
				chars.add(PMDChar.find("<" + value + ">"));
			} else chars.add(PMDChar.find(text.substring(c, c + 1)));
			++c;
		}

		for (c = 0; c < chars.size(); ++c)
			if (chars.get(c) == null) chars.remove(c);
		return chars;
	}

	/** Renders the input message at the topright x, y coordinates. */
	public void render(Graphics2D g, Message message, int x, int y)
	{
		this.render(g, message.toString(), x, y);
	}

	/** Renders the input text at the topright x, y coordinates. */
	public void render(Graphics2D g, String text, int x, int y)
	{
		ArrayList<PMDChar> chars = this.decode(text);
		for (PMDChar c : chars)
		{
			g.drawImage(this.sprites.get(c), x, y, null);
			x += c.width;
		}
	}

	/** Transforms a String into a printable array of strings printable to the screen. */
	public ArrayList<String> splitLines(String text, int boxwidth)
	{
		ArrayList<String> textlines = new ArrayList<>();
		if (text == null) return textlines;
		int currentlength = 0, iterator = 0;
		String[] parts = text.split("\n");
		for (int i = 0; i < parts.length; i++)
		{
			textlines.add("");
			currentlength = 0;
			String[] words = parts[i].split(" ");
			for (int j = 0; j < words.length; j++)
				if (!words[j].startsWith(PMDChar.new_line.value) && (currentlength == 0 || currentlength + this.width(words[j]) < boxwidth))
				{
					textlines.set(iterator, textlines.get(iterator) + words[j] + " ");
					currentlength += this.width(words[j] + " ");
				} else
				{
					textlines.add(words[j] + " ");
					++iterator;
					currentlength = this.width(words[j] + " ");
				}
			++iterator;
		}
		return textlines;
	}

	/** @return The width of the input message. */
	public int width(Message message)
	{
		if (message == null) return 0;
		return this.width(message.toString());
	}

	/** @return The width of the input text. */
	public int width(String text)
	{
		if (text == null) return 0;
		ArrayList<PMDChar> chars = this.decode(text);
		int w = 0;
		for (PMDChar c : chars)
			w += c.width;
		return w;
	}

}
