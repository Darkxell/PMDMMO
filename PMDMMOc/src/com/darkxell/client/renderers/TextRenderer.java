package com.darkxell.client.renderers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.resources.images.hud.FontSpriteSet;
import com.darkxell.common.util.language.Message;

public class TextRenderer
{

	public static enum PMDChar
	{
		new_line("<br>", 0, 0, 1),
		tabulation("<tab>", 0, 0, 1),
		space(" ", 0, 0, 4),
		space_number("<spn>", 0, 0, 6),
		a("a", 1, 0, 5),
		b("b", 2, 0, 5),
		c("c", 3, 0, 5),
		d("d", 4, 0, 5),
		e("e", 5, 0, 5),
		f("f", 6, 0, 5),
		g("g", 7, 0, 5),
		h("h", 8, 0, 5),
		i("i", 9, 0, 3),
		j("j", 10, 0, 3),
		k("k", 11, 0, 5),
		l("l", 12, 0, 3),
		m("m", 13, 0, 8),
		n("n", 14, 0, 5),
		o("o", 15, 0, 5),
		p("p", 16, 0, 5),
		q("q", 17, 0, 5),
		r("r", 18, 0, 5),
		s("s", 19, 0, 5),
		t("t", 0, 1, 4),
		u("u", 1, 1, 5),
		v("v", 2, 1, 6),
		w("w", 3, 1, 8),
		x("x", 4, 1, 6),
		y("y", 5, 1, 5),
		z("z", 6, 1, 5),
		A("A", 7, 1, 6),
		B("B", 8, 1, 6),
		C("C", 9, 1, 6),
		D("D", 10, 1, 6),
		E("E", 11, 1, 5),
		F("F", 12, 1, 5),
		G("G", 13, 1, 6),
		H("H", 14, 1, 6),
		I("I", 15, 1, 4),
		J("J", 16, 1, 6),
		K("K", 17, 1, 6),
		L("L", 18, 1, 5),
		M("M", 19, 1, 8),
		N("N", 0, 2, 6),
		O("O", 1, 2, 6),
		P("P", 2, 2, 6),
		Q("Q", 3, 2, 6),
		R("R", 4, 2, 6),
		S("S", 5, 2, 6),
		T("T", 6, 2, 6),
		U("U", 7, 2, 6),
		V("V", 8, 2, 6),
		W("W", 9, 2, 10),
		X("X", 10, 2, 6),
		Y("Y", 11, 2, 6),
		Z("Z", 12, 2, 6),
		num1("1", 13, 2, 6),
		num2("2", 14, 2, 6),
		num3("3", 15, 2, 6),
		num4("4", 16, 2, 6),
		num5("5", 17, 2, 6),
		num6("6", 18, 2, 6),
		num7("7", 19, 2, 6),
		num8("8", 0, 3, 6),
		num9("9", 1, 3, 6),
		num0("0", 2, 3, 6),
		column(":", 3, 3, 2),
		plus("+", 4, 3, 6),
		minus("-", 5, 3, 5),
		coma(",", 6, 3, 3),
		dot(".", 7, 3, 3),
		exclamation_("<!>", 8, 3, 4),
		exclamation("!", 9, 3, 4),
		interrogation_("<?>", 10, 3, 6),
		interrogation("?", 11, 3, 6),
		apostrophe_("`", 12, 3, 3),
		apostrophe("'", 13, 3, 3),
		quote_("<\">", 14, 3, 6),
		quote("\"", 15, 3, 6),
		male("<male>", 16, 3, 8),
		female("<female>", 17, 3, 6),
		space_visible("_", 18, 3, 10),
		three_dots("<dots>", 19, 3, 9),
		parenthesis_o("(", 0, 4, 5),
		parenthesis_c(")", 1, 4, 5),
		slash("/", 2, 4, 5),
		poke1("<poke1>", 3, 4, 8),
		poke2("<poke2>", 4, 4, 7),
		e_accent("é", 5, 4, 7),
		tilde("~", 6, 4, 6),
		sharp("#", 7, 4, 9),
		music("<music>", 8, 4, 8),
		arrow_up("<arrow-up>", 9, 4, 8),
		arrow_right("<arrow-right>", 10, 4, 8),
		arrow_down("<arrow-down>", 11, 4, 8),
		arrow_left("<arrow-left>", 12, 4, 8),
		percent("%", 13, 4, 9),
		tick("<tick>", 14, 4, 9),
		star("<star>", 15, 4, 7),
		glued("<glued>", 16, 4, 8),
		mission("<mission>", 17, 4, 8),
		mission_accepted("<mission-a>", 18, 4, 10),
		news("<news>", 19, 4, 10),
		story("<story>", 0, 5, 9),
		speech_bubble("<speech>", 1, 5, 9),
		key_A("<key-a>", 2, 5, 10),
		key_B("<key-b>", 3, 5, 10),
		key_L("<key-l>", 4, 5, 10),
		key_R("<key-r>", 5, 5, 10),
		key_PLUS("<key-+>", 6, 5, 10),
		key_SELECT1("<select1>", 7, 5, 10),
		key_SELECT2("<select2>", 8, 5, 10),
		buy_1("<1>", 9, 5, 6),
		buy_2("<2>", 10, 5, 6),
		buy_3("<3>", 11, 5, 6),
		buy_4("<4>", 12, 5, 6),
		buy_5("<5>", 13, 5, 6),
		buy_6("<6>", 14, 5, 6),
		buy_7("<7>", 15, 5, 6),
		buy_8("<8>", 16, 5, 6),
		buy_9("<9>", 17, 5, 6),
		buy_0("<0>", 18, 5, 6),
		orb("<orb>", 19, 5, 10),
		tm_used("<tmu>", 0, 6, 10),
		hm("<hm>", 1, 6, 10),
		tm_0("<tm0>", 2, 6, 10),
		tm_1("<tm1>", 3, 6, 10),
		tm_2("<tm2>", 4, 6, 10),
		tm_3("<tm3>", 5, 6, 10),
		tm_4("<tm4>", 6, 6, 10),
		tm_5("<tm5>", 7, 6, 10),
		tm_6("<tm6>", 8, 6, 10),
		tm_7("<tm7>", 9, 6, 10),
		tm_8("<tm8>", 10, 6, 10),
		tm_9("<tm9>", 11, 6, 10),
		tm_10("<tm10>", 12, 6, 10),
		tm_11("<tm11>", 13, 6, 10),
		tm_12("<tm12>", 14, 6, 10),
		tm_13("<tm13>", 15, 6, 10),
		tm_14("<tm14>", 16, 6, 10),
		tm_15("<tm15>", 17, 6, 10),
		tm_16("<tm16>", 18, 6, 10),
		tm_17("<tm17>", 19, 6, 10),
		type_0("<type-0>", 0, 7, 11),
		type_1("<type-1>", 1, 7, 11),
		type_2("<type-2>", 2, 7, 11),
		type_3("<type-3>", 3, 7, 11),
		type_4("<type-4>", 4, 7, 11),
		type_5("<type-5>", 5, 7, 11),
		type_6("<type-6>", 6, 7, 11),
		type_7("<type-7>", 7, 7, 11),
		type_8("<type-8>", 8, 7, 11),
		type_9("<type-9>", 9, 7, 11),
		type_10("<type-10>", 10, 7, 11),
		type_11("<type-11>", 11, 7, 11),
		type_12("<type-12>", 12, 7, 11),
		type_13("<type-13>", 13, 7, 11),
		type_14("<type-14>", 14, 7, 11),
		type_15("<type-15>", 15, 7, 11),
		type_16("<type-16>", 16, 7, 11),
		type_17("<type-17>", 17, 7, 11),
		minusd("<-d>", 0, 8, 7),
		num1d("<1d>", 1, 8, 7),
		num2d("<2d>", 2, 8, 7),
		num3d("<3d>", 3, 8, 7),
		num4d("<4d>", 4, 8, 7),
		num5d("<5d>", 5, 8, 7),
		num6d("<6d>", 6, 8, 7),
		num7d("<7d>", 7, 8, 7),
		num8d("<8d>", 8, 8, 7),
		num9d("<9d>", 9, 8, 7),
		num0d("<0d>", 10, 8, 7),
		plush("<+h>", 0, 9, 7),
		num1h("<1h>", 1, 9, 7),
		num2h("<2h>", 2, 9, 7),
		num3h("<3h>", 3, 9, 7),
		num4h("<4h>", 4, 9, 7),
		num5h("<5h>", 5, 9, 7),
		num6h("<6h>", 6, 9, 7),
		num7h("<7h>", 7, 9, 7),
		num8h("<8h>", 8, 9, 7),
		num9h("<9h>", 9, 9, 7),
		num0h("<0h>", 10, 9, 7),
		pluse("<+e>", 0, 10, 7),
		num1e("<1e>", 1, 10, 7),
		num2e("<2e>", 2, 10, 7),
		num3e("<3e>", 3, 10, 7),
		num4e("<4e>", 4, 10, 7),
		num5e("<5e>", 5, 10, 7),
		num6e("<6e>", 6, 10, 7),
		num7e("<7e>", 7, 10, 7),
		num8e("<8e>", 8, 10, 7),
		num9e("<9e>", 9, 10, 7),
		num0e("<0e>", 10, 10, 7),
		bracketOpen("[", 11, 8, 5),
		bracketClose("]", 12, 8, 5),
		colorReset("</color>", -1, -1, 0),
		colorBlue("<blue>", -1, -1, 0),
		colorGreen("<green>", -1, -1, 0),
		colorRed("<red>", -1, -1, 0),
		colorYellow("<yellow>", -1, -1, 0);

		public static PMDChar find(String value)
		{
			for (PMDChar c : values())
				if (c.value != null && c.value.equals(value)) return c;
			return null;
		}

		/** Location on the Font sprite. */
		public final int xPos, yPos;
		public final String value;
		/** Width of the sprite. */
		public final int width;

		private PMDChar(String value, int x, int y, int width)
		{
			this.value = value;
			this.xPos = x;
			this.yPos = y;
			this.width = width;
		}

		/** @return <code>true</code> if this is an actual displayable character. Chars that may return false are for example color marks, tabs, etc. */
		public boolean isChar()
		{
			return this.xPos >= 0 && this.yPos >= 0;
		}

	}

	public static enum FontMode
	{
		NORMAL,
		EXPERIENCE,
		DAMAGE,
		HEAL;
	}

	private static Color color, previous;
	private static final HashMap<PMDChar, Image> coloredSprites = new HashMap<>();
	private static final HashMap<PMDChar, PMDChar> expChars = new HashMap<>();
	private static final HashMap<PMDChar, PMDChar> damageChars = new HashMap<>();
	private static final HashMap<PMDChar, PMDChar> healChars = new HashMap<>();
	public static double fontSize = 1;
	private static final int LINE_SPACING = 3;
	private static final int TAB_ALIGN = 25;

	static
	{
		damageChars.put(PMDChar.minus, PMDChar.minusd);
		damageChars.put(PMDChar.num1, PMDChar.num1d);
		damageChars.put(PMDChar.num2, PMDChar.num2d);
		damageChars.put(PMDChar.num3, PMDChar.num3d);
		damageChars.put(PMDChar.num4, PMDChar.num4d);
		damageChars.put(PMDChar.num5, PMDChar.num5d);
		damageChars.put(PMDChar.num6, PMDChar.num6d);
		damageChars.put(PMDChar.num7, PMDChar.num7d);
		damageChars.put(PMDChar.num8, PMDChar.num8d);
		damageChars.put(PMDChar.num9, PMDChar.num9d);
		damageChars.put(PMDChar.num0, PMDChar.num0d);

		healChars.put(PMDChar.plus, PMDChar.plush);
		healChars.put(PMDChar.num1, PMDChar.num1h);
		healChars.put(PMDChar.num2, PMDChar.num2h);
		healChars.put(PMDChar.num3, PMDChar.num3h);
		healChars.put(PMDChar.num4, PMDChar.num4h);
		healChars.put(PMDChar.num5, PMDChar.num5h);
		healChars.put(PMDChar.num6, PMDChar.num6h);
		healChars.put(PMDChar.num7, PMDChar.num7h);
		healChars.put(PMDChar.num8, PMDChar.num8h);
		healChars.put(PMDChar.num9, PMDChar.num9h);
		healChars.put(PMDChar.num0, PMDChar.num0h);

		expChars.put(PMDChar.plus, PMDChar.pluse);
		expChars.put(PMDChar.num1, PMDChar.num1e);
		expChars.put(PMDChar.num2, PMDChar.num2e);
		expChars.put(PMDChar.num3, PMDChar.num3e);
		expChars.put(PMDChar.num4, PMDChar.num4e);
		expChars.put(PMDChar.num5, PMDChar.num5e);
		expChars.put(PMDChar.num6, PMDChar.num6e);
		expChars.put(PMDChar.num7, PMDChar.num7e);
		expChars.put(PMDChar.num8, PMDChar.num8e);
		expChars.put(PMDChar.num9, PMDChar.num9e);
		expChars.put(PMDChar.num0, PMDChar.num0e);
	}

	private TextRenderer()
	{}

	/** Adds spaces in front of the input number to match the input number of digits. */
	public static Message alignNumber(long n, int digits)
	{
		String s = Long.toString(n);
		int diff = s.length() - digits;
		for (int i = diff; i < 0; ++i)
			s = PMDChar.space_number.value + s;
		return new Message(s, false);
	}

	public static ArrayList<PMDChar> decode(String text)
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

		chars.removeIf(ch -> ch == null);
		return chars;
	}

	public static Color getColor()
	{
		return color;
	}

	public static int height()
	{
		return (int) (FontSpriteSet.CHAR_HEIGHT * fontSize);
	}

	public static int lineSpacing()
	{
		return (int) (LINE_SPACING * fontSize);
	}

	/** Renders the input text at the topright x, y coordinates. */
	public static void render(Graphics2D g, List<PMDChar> text, int x, int y)
	{
		int w = 0;
		for (PMDChar c : text)
		{
			if (!c.isChar())
			{
				if (c == PMDChar.colorReset) setColor(null);
				if (c == PMDChar.colorBlue) setColor(Palette.FONT_BLUE);
				if (c == PMDChar.colorGreen) setColor(Palette.FONT_GREEN);
				if (c == PMDChar.colorRed) setColor(Palette.FONT_RED);
				if (c == PMDChar.colorYellow) setColor(Palette.FONT_YELLOW);
			} else
			{
				Image sprite = color == null ? Sprites.Hud.font.getImg(c) : coloredSprites.get(c);
				g.drawImage(sprite, x + w, y, (int) (sprite.getWidth(null) * fontSize), (int) (sprite.getHeight(null) * fontSize), null);
				if (c == PMDChar.tabulation) w += tabWidth(w);
				else w += width(c);
			}
		}
		setColor(null);
	}

	/** Renders the input message at the topright x, y coordinates. */
	public static void render(Graphics2D g, Message message, int x, int y)
	{
		render(g, message.toString(), x, y);
	}

	public static void render(Graphics2D g, String text, int x, int y)
	{
		render(g, text, x, y, FontMode.NORMAL);
	}

	/** Renders the input text at the topright x, y coordinates. */
	public static void render(Graphics2D g, String text, int x, int y, FontMode font)
	{
		ArrayList<PMDChar> chars = decode(text);
		ArrayList<PMDChar> toprint = new ArrayList<TextRenderer.PMDChar>();
		HashMap<PMDChar, PMDChar> fontmode = null;
		switch (font)
		{
			case DAMAGE:
				fontmode = damageChars;
				break;

			case EXPERIENCE:
				fontmode = expChars;
				break;

			case HEAL:
				fontmode = healChars;
				break;

			case NORMAL:
			default:
				break;
		}
		if (fontmode != null) for (PMDChar c : chars)
			if (fontmode.containsKey(c)) toprint.add(fontmode.get(c));
			else toprint.add(c);
		else toprint.addAll(chars);
		render(g, toprint, x, y);
	}

	/** Sets the Color of the Text for the next drawn Text. Color is reset to default at the end of each Text. */
	public static void setColor(final Color color)
	{
		boolean shouldUpdate = color != null && color != previous;
		previous = color;
		TextRenderer.color = color;
		if (shouldUpdate)
		{
			RGBImageFilter filter = new RGBImageFilter() {
				@Override
				public int filterRGB(int x, int y, int rgb)
				{
					return rgb == 0xFFFFFFFE ? color.getRGB() : rgb;
				}
			};

			for (PMDChar c : PMDChar.values())
				if (c.isChar())
					coloredSprites.put(c, Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(Sprites.Hud.font.getImg(c).getSource(), filter)));
		}
	}

	/** Transforms a String into an array of strings printable to the screen. */
	public static ArrayList<String> splitLines(String text, int boxwidth)
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
				if (!words[j].startsWith(PMDChar.new_line.value) && (currentlength == 0 || currentlength + width(words[j]) < boxwidth))
				{
					textlines.set(iterator, textlines.get(iterator) + words[j] + " ");
					currentlength += width(words[j] + " ");
				} else
				{
					textlines.add(words[j] + " ");
					++iterator;
					currentlength = width(words[j] + " ");
				}
			++iterator;
		}
		return textlines;
	}

	/** @return The width of a Tabulation character, depending on the current width of the text. */
	public static int tabWidth(int currentWidth)
	{
		return (int) (TAB_ALIGN * fontSize - currentWidth % TAB_ALIGN * fontSize);
	}

	public static int width(List<PMDChar> chars)
	{
		int w = 0;
		for (PMDChar c : chars)
			if (c == PMDChar.tabulation) w += tabWidth(w);
			else w += width(c);
		return w;
	}

	/** @return The width of the input message. */
	public static int width(Message message)
	{
		if (message == null) return 0;
		return width(message.toString());
	}

	public static int width(PMDChar c)
	{
		return (int) (c.width * fontSize);
	}

	/** @return The width of the input text. */
	public static int width(String text)
	{
		if (text == null) return 0;
		return width(decode(text));
	}

}
