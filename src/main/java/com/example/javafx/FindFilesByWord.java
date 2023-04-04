package com.example.javafx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.Scene;
import javafx.stage.*;

public class FindFilesByWord extends Application
{

	private Stage stage;
	private BorderPane bp;
	private TextField txtSearch, txtPath;
	private Label lblSearch, lblPath;
	private Button cmdBrowse, cmdSearch, cmdClear;
	private ComboBox<String> cbo;
	private File[] baseFiles;
//	private File[] foundFiles;
	private ScrollPane spFiles;
	private HBox dispImgHB;
	private ListView<String> lvFiles;
	private ImageView wpIV;
	private Image wp;
	private Image[] wpArr;
	private List<SearchCriteria> scList;
	private TextArea selFileTA;
	private HBox dispSFLTA;
	private VBox centerBox;
	private List<Menu> mList;
	private List<File> ffList;
	private List<String> fnList;


	@Override
	public void start(Stage stage)
	{
		scList = new ArrayList<>();
		bp = new BorderPane();


		bp.setTop(estMenuBar());
		bp.setCenter(getCenter());
		bp.setLeft(getLeftImg());


//		txtSearch.setText("Pane");
//		txtPath.setText("D:/OneDrive/Desktop/AllCodeMar26");


//		right = new ScrollPane();
//		right.setVisible(false);
//		right.setMaxWidth(1);

//		bp.setRight(right);

//		bgPane.getChildren().add(bp);

		setButtons();
		setFontStyle();
		Scene scene = new Scene(bp, 800, 400);

		stage.setScene(scene);
		stage.setTitle("FileFinder");
		stage.show();

	}


	public HBox getLeftImg()
	{

		dispImgHB = getHB(0, 10);
		dispImgHB.setMaxWidth(350);

		wp = new Image("D:/intelliJ_IDEA/intelliJ_java/JavaFX/src/main/resources/com/example/javafx/win11lightblue.jpg");

		wpIV = new ImageView(wp);

		wpIV.setFitWidth(300);
		wpIV.setFitHeight(400);

		dispImgHB.getChildren().add(wpIV);


		return dispImgHB;
	}

	public void findFiles(SearchCriteria sc) throws IOException
	{

		File dir = new File(sc.getPath());    //<-------done

		if (!dir.exists())
		{
			System.out.println("Doesn't exist");
		}

		ffList = new ArrayList<>();
		//get the found files into the foundFiles array
//		getFoundFiles(contents, sc);

		loopDirFindFiles(dir, sc);
		//creates the directory with the name of the search term

		File foundDir = new File(sc.getPath() + "\\" + sc.getFolderName());
		foundDir.mkdir();

		writeFoundFiles(ffList, foundDir, sc);

	}

	/**
	 * I need to take in a directory
	 * make sure directory is not null
	 * for loop to look at each file in the passed-in directory
	 * if file is not a directory,
	 * add to fflist
	 * /
	 * if file is a directory,
	 * assert listFiles != null
	 * for each dir in passed-in directory.listFiles
	 * recursive call(dir, sc)
	 */
	public void loopDirFindFiles(File directory, SearchCriteria sc) throws FileNotFoundException
	{
		assert directory.listFiles() != null;

		for (File file : Objects.requireNonNull(directory.listFiles()))
		{
			if (file.isFile())
			{
				scanFilesAddIfMatch(file, sc);
			}
			if (file.isDirectory())
			{
				for (File f : Objects.requireNonNull(file.listFiles()))
				{
					loopDirFindFiles(file, sc);
				}
			}
		}

	}

	public void scanFilesAddIfMatch(File file, SearchCriteria sc) throws FileNotFoundException
	{
		if (file.getName().endsWith(sc.getFileType()))    //<-------done
			{
				try (Scanner scan = new Scanner(file))
				{
					boolean b = false;

					while (scan.hasNextLine())
					{
						String searchWord = scan.nextLine();
						for (String s : sc.getSearch())
						{
							b = searchWord.contains(s);

							if (b)
							{
								ffList.add(file);
//								foundFiles = push(foundFiles, f);
								break;
							}
						}
						if (b)
						{
							break;
						}

					}
				}
			}
	}


//	public void getFoundFiles(File[] allFiles, SearchCriteria sc) throws FileNotFoundException
//	{
//
//		for (File f : allFiles)
//		{
//			if (f.getName().endsWith(sc.getFileType()))    //<-------done
//			{
//				try (Scanner scan = new Scanner(f))
//				{
//					boolean b = false;
//
//					while (scan.hasNextLine())
//					{
//						String str = scan.nextLine();
//						for (String s : sc.getSearch())
//						{
//							b = str.contains(s);
//
//							if (b)
//							{
//								ffList.add(f);
////								foundFiles = push(foundFiles, f);
//								break;
//							}
//						}
//						if (b)
//						{
//							break;
//						}
//
//					}
//				}
//			}
//		}
//	}

//	public File[] push(File[] array, File file)
//	{
//		File[] retArr = new File[array.length + 1];
//
//		for (int i = 0; i < array.length; i++)
//		{
//			retArr[i] = array[i];
//		}
//
//		retArr[array.length] = file;
//
//		return retArr;
//
//	}
































	public void writeFoundFiles(List<File> ff, File d, SearchCriteria sc) throws IOException
	{
		//for each of the files that were previously found
		for (File f : ff)
		{
			//creates new file with the same path name and file type

			String ffName = f.getName().substring(0, f.getName().indexOf(".")) + "-ff" + sc.getFileType();

			File found = new File(d.getAbsolutePath() + "\\" + ffName);

			//scans the
			try (Scanner scan = new Scanner(f); PrintWriter pw = new PrintWriter(found))
			{
				while (scan.hasNextLine())
				{
					pw.write(scan.nextLine() + "\n");
				}
			}

		}
	}


	public void setButtons()
	{
		cmdBrowse.setOnAction(e ->
		{
			initBrowse();
		});

		cmdSearch.setOnAction(e ->
		{
			SearchCriteria sc = new SearchCriteria(txtSearch.getText(), cbo.getValue(), txtPath.getText());

			File baseDir = new File(sc.getPath());

			int nc = 0;

			for (int i=0; i<baseDir.listFiles().length; i++)
			{
				File[] bd = baseDir.listFiles();


					assert bd != null;
					if (bd[i].getName().equals(String.format("~%s~", sc.getSearchStr())))
					{
						nc++;
					}

			}

			if (nc > 0)
			{
				sc.setFolderName(String.format("~%s (%s)~",sc.getSearchStr()));
			} else {
				sc.setFolderName(sc.getSearchStr());
			}


			scList.add(sc);

			try
			{
				findFiles(sc);
			} catch (IOException ex)
			{
				throw new RuntimeException(ex);
			}

			ExecutorService pool = Executors.newCachedThreadPool();

			IncCounter inc = new IncCounter();
			IncCounter inc2 = new IncCounter();
			inc.setCount(300);
			inc2.setCount(100);

			pool.execute(() ->
			{
				lvFiles = new ListView<>();

				for (File f : ffList)
				{
					lvFiles.getItems().add(f.getName());
				}

				lvFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

				spFiles = new ScrollPane(lvFiles);


				Platform.runLater(() ->
				{
					while (inc.getCount() > 150 && inc2.getCount() < 400)
					{
						try
						{
							spFiles.setPrefWidth(inc2.getAndUpdate());
							wpIV.setFitWidth(inc.getAndDec());

						} catch (Exception ex)
						{
							throw new RuntimeException(ex);
						}
					}


					lvFiles.getSelectionModel().selectedItemProperty().addListener(event ->
					{

						FileChooser obOpen = new FileChooser();

						obOpen.setTitle("Open File for Processing");

						String filename = sc.getPath() + "/" + lvFiles.getSelectionModel().getSelectedItem();

						System.out.println(filename);
//						obOpen.setInitialDirectory(new File("" + lvFiles.getSelectionModel().selectedItemProperty()));

						File file = new File(filename);

						// creating the file that the user chooses

						if (file != null)
						{
							Stage txtStage = new Stage();
							Pane txtPane = new Pane();
							TextArea taFile = new TextArea(getContents(file));
							txtPane.getChildren().add(taFile);

							taFile.prefWidthProperty().bind(txtPane.widthProperty());
							taFile.prefHeightProperty().bind(txtPane.heightProperty());

							taFile.setFont(Font.font("Consolas", FontWeight.NORMAL, FontPosture.REGULAR, 16));

							Scene txtScene = new Scene(txtPane, 900, 1100);

							txtStage.setScene(txtScene);
							txtStage.show();

						}

					});

					spFiles.setMaxHeight(400);
					lvFiles.setMaxHeight(398);
					lvFiles.setMaxWidth(247);

					bp.setLeft(wpIV);
					bp.setRight(spFiles);
				});
			});

		});


	}


	public void setFontStyle()
	{
		lblSearch.setFont(Font.font("Amsi Pro AKS Condensed SemiBold", FontWeight.BOLD, FontPosture.REGULAR, 22));
		lblPath.setFont(Font.font("Amsi Pro AKS Condensed SemiBold", FontWeight.BOLD, FontPosture.REGULAR, 22));
	}

	public VBox getCenter()
	{
		centerBox = getVB(20, 10);
		VBox searchBox = getSearchBox();
		VBox pathBox = getPathBox();
		HBox subBox = getSubBox();

		centerBox.getChildren().addAll(searchBox, pathBox, subBox);

		return centerBox;
	}


	/**
	 * Search Button
	 * @return - used in setCenter()
	 */
	public HBox getSubBox()
	{
		HBox subHB = getHB(20, 10);
		cmdSearch = new Button("Search");
		subHB.getChildren().add(cmdSearch);

		return subHB;
	}

	public VBox getSearchBox()
	{
		VBox searchBox = getVB(10, 10);
		lblSearch = new Label("Enter search criteria");

		HBox searchHB = getHB(5, 10);
		txtSearch = new TextField();
		txtSearch.setPrefWidth(220);

		cbo = getFileCBO();
		cbo.setMaxWidth(68);

		searchHB.getChildren().addAll(txtSearch, cbo);

		searchBox.getChildren().addAll(lblSearch, searchHB);

		return searchBox;
	}

	public VBox getPathBox()
	{
		VBox pathBox = getVB(10, 10);
		lblPath = new Label("Enter directory path");

		HBox pathHB = getHB(5, 10);
		txtPath = new TextField();
		txtPath.setPrefWidth(235);

		cmdBrowse = new Button("Browse");


		pathHB.getChildren().addAll(txtPath, cmdBrowse);

		pathBox.getChildren().addAll(lblPath, pathHB);

		return pathBox;
	}

	public ComboBox<String> getFileCBO()
	{
		cbo = new ComboBox<>();
		cbo.getItems().addAll(".java", ".html", ".css", ".js", ".txt", ".csv", ".*");
		cbo.setValue(".java");

		return cbo;
	}

	public void initBrowse()
	{
		DirectoryChooser dc = new DirectoryChooser();
		dc.setInitialDirectory(new File("D:/OneDrive/Desktop/AllCodeMar26"));

		File dir = dc.showDialog(stage);
		txtPath.setText(dir.getPath());
	}

	public HBox getHB(int p, int s)
	{
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(p));
		hb.setSpacing(s);
		return hb;
	}

	public VBox getVB(int p, int s)
	{
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(p));
		vb.setSpacing(s);
		return vb;
	}


	public MenuBar estMenuBar()
	{
		mList = new ArrayList<>();

		MenuBar obBar = new MenuBar();
		obBar.setPadding(new Insets(0));
//		obBar.setStyle("-fx-background-color: rgb(220,220,220)");
		obBar.getMenus().add(getFile());
		obBar.getMenus().add(getEdit());
		obBar.getMenus().add(getHelp());

//		for (Menu m : mList)
//		{
//
//		}

		return obBar;
	}

	/**
	 * This method will return an appropriate Menu item for adding to a MenuBar
	 * Menus can contain MenuItems
	 *
	 * @return
	 */
	public Menu getFile()
	{
		Menu mnFile = new Menu("File");

		MenuItem miOpen = new MenuItem("Open");
		MenuItem miSave = new MenuItem("Save");
		SeparatorMenuItem obSep = new SeparatorMenuItem();
		MenuItem miExit = new MenuItem("Exit");

		mnFile.getItems().addAll(miOpen, miSave, obSep, miExit);

//		miOpen.setOnAction(e -> dealWithOpen());

//		miSave.setOnAction(e -> dealWithSave());

		miExit.setOnAction(e -> System.exit(0));

		mList.add(mnFile);
		return mnFile;
	}

	public Menu getEdit()
	{
		Menu mnEdit = new Menu("Edit");
		Menu mnBack = new Menu("Background Color");
		MenuItem miSelect = new MenuItem("Select");
		MenuItem miRandom = new MenuItem("Random");

		mnBack.getItems().addAll(miSelect, miRandom);
		mnEdit.getItems().add(mnBack);

		miSelect.setOnAction(e ->
		{
//			setBG();

		});
		miRandom.setOnAction(e ->
		{
//
//			new Thread(() -> {
//				while (true)
//				{
//					double r = Math.random() * 1;
//					double g = Math.random() * 1;
//					double b = Math.random() * 1;
//
//					System.out.printf("rgb(%.2f,%.2f,%.2f)", r, g, b);
//
//					Color obColor = new Color(r, g, b, 1);
//
//					String sRGB = String.format("rgb(%.0f,%.0f,%.0f)", (r * 255), (g * 255), (b * 255));
//					//
//					System.out.println(sRGB);
//
//
//
//
//					Platform.runLater(() -> {
//						centerBox.setStyle("-fx-control-inner-background:" + sRGB);
//					});
//
//					try
//					{
//						Thread.sleep(1000);
//					}
//					catch (InterruptedException e1)
//					{
//						e1.printStackTrace();
//					}
//				}
//
//			}).start();

		});

		mList.add(mnEdit);
		return mnEdit;
	}

//	/**
//	 * This routine will use a color picker to go ahead and select a background
//	 * color for our TextArea. A color picker will give us a dialog box that we can
//	 * use to select from a list of colors.
//	 */
//	public void setBG()
//	{
//		ColorPicker obPicker = new ColorPicker();
//		HBox obBox = new HBox(obPicker);
//		obBox.setAlignment(Pos.CENTER);
//
//		bp.setCenter(obBox);
//
//		obPicker.setOnAction(e -> {
//			Color obColor = obPicker.getValue();
//
//			System.out.printf("%.2f,%.2f,%.2f", obColor.getRed(), obColor.getGreen(), obColor.getBlue());
//
//			// The way we set the color for a TextArea is to use the following:
//			//// fx-control-inner-background
//			// we are going to build up an RGB string to represent this color
//
//			String sRGB = String.format("rgb(%.2f,%.2f,%.2f)",
//					obColor.getRed() * 255, obColor.getGreen() * 255,  obColor.getBlue() * 255);
//			.setStyle("-fx-control-inner-background:" + sRGB);
//			bp.setCenter(getCenter());
//
//		});
//	}


//	public void dealWithSave()
//	{
//		FileChooser obSave = new FileChooser();
//
//		obSave.setTitle("Save File");
//		obSave.setInitialDirectory(new File("D:/TestArea"));
//
//		obSave.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"),
//				new FileChooser.ExtensionFilter("All Files", "*.*"));
//
//		File obFile = obSave.showSaveDialog(obStage);
//
//		if (obFile != null)
//		{
//			try (PrintWriter obPW = new PrintWriter(obFile))
//			{
//				obPW.write(obContents.getText());
//			} catch (FileNotFoundException e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//
//
//	/**
//	 * This will introduce us to the notion of working with OpenFile dialogs that
//	 * are used to indicate which files to open for processing.
//	 *
//	 * @throws FileNotFoundException
//	 * @throws
//	 */
//	public void dealWithOpen()
//	{
//		FileChooser obOpen = new FileChooser();
//
//		obOpen.setTitle("Open File for Processing");
//		obOpen.setInitialDirectory(new File("D:/TestArea"));
//
//		// Normally open dialogs will restrict themselves initially only to a certain
//		// type of file
//		obOpen.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt files", "*.txt"),
//				new FileChooser.ExtensionFilter("All Files", "*.*"));
//
//		// creating the file that the user chooses
//		File obFile = obOpen.showOpenDialog(stage);
//
//		if (obFile != null)
//		{
//
////
////			centerBox.setText(getContents(obFile));
//
//		}
//
//	}

	public String getContents(File obFile)
	{

		String str = "";

		try (Scanner obIn = new Scanner(obFile))
		{
			int lc = 0;
			while (obIn.hasNextLine())
			{
				str += lc++ + "\t" + obIn.nextLine().replaceAll("\t", "\s\s\s") + "\n";
			}
			return str;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Menu getHelp()
	{
		Menu mnHelp = new Menu("Help");

		MenuItem miAbout = new MenuItem("about");

		mnHelp.getItems().addAll(miAbout);

		mnHelp.setOnAction(e ->
		{
			Alert alHelp = new Alert(Alert.AlertType.INFORMATION);
			alHelp.setContentText("Program for Displaying Menus");
			alHelp.setHeaderText("Author: Carter Walsh");
			alHelp.setTitle("Vanity");
			alHelp.showAndWait();
		});
		mList.add(mnHelp);
		return mnHelp;
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}

}
