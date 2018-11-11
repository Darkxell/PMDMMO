package fr.darkxell.dataeditor.application.data;

public class DialogOptionTableItem
{

	public String message;
	public Boolean translate;

	public DialogOptionTableItem(String message, Boolean translate)
	{
		this.message = message;
		this.translate = translate;
	}

	public String getMessage()
	{
		return this.message;
	}

	public String getTranslate()
	{
		return String.valueOf(this.translate);
	}

}
