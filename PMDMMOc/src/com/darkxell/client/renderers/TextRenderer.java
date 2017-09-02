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
		a("a", 1, 5),
		A("A", 26, 6),
		apostrophe("'", 72, 3),
		apostrophe_("`", 71, 3),
		b("b", 2, 5),
		B("B", 27, 6),
		buy_0("<0>", 112, 6),
		buy_1("<1>", 103, 6),
		buy_2("<2>", 104, 6),
		buy_3("<3>", 105, 6),
		buy_4("<4>", 106, 6),
		buy_5("<5>", 107, 6),
		buy_6("<6>", 108, 6),
		buy_7("<7>", 109, 6),
		buy_8("<8>", 110, 6),
		buy_9("<9>", 111, 6),
		c("c", 3, 5),
		C("C", 28, 6),
		column(":", 62, 2),
		coma(",", 65, 3),
		d("d", 4, 5),
		D("D", 29, 6),
		dot(".", 66, 3),
		e("e", 5, 5),
		E("E", 30, 5),
		e_accent("é", 84, 7),
		exclamation("!", 68, 4),
		exclamation_("<!>", 67, 4),
		f("f", 6, 5),
		F("F", 31, 5),
		female("<female>", 76, 6),
		g("g", 7, 5),
		G("G", 32, 6),
		h("h", 8, 5),
		H("H", 33, 6),
		i("i", 9, 3),
		I("I", 34, 4),
		interrogation("?", 70, 6),
		interrogation_("<?>", 69, 6),
		j("j", 10, 3),
		J("J", 35, 6),
		k("k", 11, 5),
		K("K", 36, 6),
		key_A("<key-a>", 94, 9),
		key_B("<key-b>", 95, 10),
		key_L("<key-l>", 96, 10),
		key_PLUS("<key-+>", 98, 10),
		key_R("<key-r>", 97, 10),
		key_SELECT1("<select1>", 99, 10),
		key_SELECT2("<select2>", 100, 10),
		l("l", 12, 3),
		L("L", 37, 5),
		m("m", 13, 8),
		M("M", 38, 8),
		male("<male>", 75, 8),
		minus("-", 64, 5),
		mission("<mission>", 90, 8),
		mission_accepted("<mission-a>", 91, 10),
		music("<music>", 87, 8),
		n("n", 14, 5),
		N("N", 39, 6),
		news("<news>", 92, 10),
		num0("0", 61, 6),
		num1("1", 52, 6),
		num2("2", 53, 6),
		num3("3", 54, 6),
		num4("4", 55, 6),
		num5("5", 56, 6),
		num6("6", 57, 6),
		num7("7", 58, 6),
		num8("8", 59, 6),
		num9("9", 60, 6),
		o("o", 15, 5),
		O("O", 40, 6),
		orb("<orb>", 102, 10),
		p("p", 16, 5),
		P("P", 41, 6),
		parenthesis_c(")", 79, 5),
		parenthesis_o("(", 80, 5),
		plus("+", 63, 6),
		poke1("<poke1>", 82, 8),
		poke2("<poke2>", 83, 7),
		q("q", 17, 5),
		Q("Q", 42, 6),
		quote("\"", 74, 6),
		quote_("<\">", 73, 6),
		r("r", 18, 5),
		R("R", 43, 6),
		s("s", 19, 5),
		S("S", 44, 6),
		sharp("#", 86, 9),
		slash("/", 81, 5),
		space(" ", 0, 4),
		space_visible("_", 77, 10),
		star("<star>", 89, 7),
		story("<story>", 93, 9),
		t("t", 20, 4),
		T("T", 45, 6),
		three_dots("<dots>", 78, 9),
		tick("<tick>", 88, 9),
		tilde("~", 85, 6),
		tm("<tm>", 101, 10),
		u("u", 21, 5),
		U("U", 46, 6),
		v("v", 22, 6),
		V("V", 47, 6),
		w("w", 22, 8),
		W("W", 48, 10),
		x("x", 23, 6),
		X("X", 49, 6),
		y("y", 24, 5),
		Y("Y", 50, 6),
		z("z", 25, 5),
		Z("Z", 51, 6);

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
	public static final TextRenderer instance = new TextRenderer();

	private HashMap<PMDChar, BufferedImage> sprites;

	private TextRenderer()
	{
		this.sprites = new HashMap<TextRenderer.PMDChar, BufferedImage>();
		BufferedImage source = Res.getBase("resources/hud/font.png");
		for (PMDChar c : PMDChar.values())
			this.sprites.put(c, Res.createimage(source, c.id % GRID_COLS, c.id / GRID_COLS, GRID_WIDTH, GRID_HEIGHT));
	}

	private ArrayList<PMDChar> decode(String text)
	{
		ArrayList<PMDChar> chars = new ArrayList<TextRenderer.PMDChar>();
		int c = 0;
		String value;
		while (c < text.length())
		{
			if (text.charAt(c) == '<')
			{
				value = "";
				++c;
				while (text.charAt(c) != '>')
				{
					value += text.charAt(c);
					++c;
				}
				chars.add(PMDChar.find(value));
			} else chars.add(PMDChar.find(text.substring(c, c + 1)));
			++c;
		}

		for (c = 0; c < chars.size(); ++c)
			if (chars.get(c) == null) chars.set(c, PMDChar.interrogation_);
		return chars;
	}

	public void render(Graphics2D g, Message message, int x, int y)
	{
		this.render(g, message.toString(), x, y);
	}

	public void render(Graphics2D g, String text, int x, int y)
	{
		ArrayList<PMDChar> chars = this.decode(text);
		for (PMDChar c : chars)
		{
			g.drawImage(this.sprites.get(c), x, y, null);
			x += c.width;
		}
	}

}
