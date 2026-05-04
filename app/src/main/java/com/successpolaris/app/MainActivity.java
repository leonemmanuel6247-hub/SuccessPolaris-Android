package com.successpolaris.app;

import android.animation.;
import android.app.;
import android.content.;
import android.graphics.;
import android.graphics.drawable.;
import android.os.;
import android.text.;
import android.view.;
import android.view.animation.;
import android.widget.;
import androidx.appcompat.app.;
import androidx.cardview.widget.;
import androidx.core.content.;
import java.util.;

public class MainActivity extends AppCompatActivity {

private LinearLayout main, catList, docGrid, navPath;
private EditText searchInput;
private TextView totalText, headerTitle;
private ScrollView scrollMain;
private String viewMode = "archives";
private List<String> navStack = new ArrayList<>();
private boolean isAdmin = false;
private String email = "", country = "Togo";
private List<Map<String,String>> categories = new ArrayList<>();
private List<Map<String,String>> documents = new ArrayList<>();
private int totalDocs = 0;

@Override
protected void onCreate(Bundle b) {
super.onCreate(b);
getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

// Init data
initData();

// Main scroll
scrollMain = new ScrollView(this);
scrollMain.setBackgroundColor(0xFF020617);
scrollMain.setFillViewport(true);

main = new LinearLayout(this);
main.setOrientation(LinearLayout.VERTICAL);
main.setPadding(20, 40, 20, 20);

// Aurora background
main.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,
new int[]{0xFF0A0020, 0xFF001030, 0xFF000510, 0xFF020617}));

// Header
LinearLayout header = buildHeader();
main.addView(header);

// Search
main.addView(buildSearch());

// Nav path
navPath = new LinearLayout(this);
navPath.setOrientation(LinearLayout.HORIZONTAL);
main.addView(navPath);

// Categories + Documents
LinearLayout content = new LinearLayout(this);
content.setOrientation(LinearLayout.VERTICAL);

catList = new LinearLayout(this);
catList.setOrientation(LinearLayout.VERTICAL);
content.addView(catList);

docGrid = new LinearLayout(this);
docGrid.setOrientation(LinearLayout.VERTICAL);
content.addView(docGrid);

main.addView(content);

// Footer
main.addView(buildFooter());

scrollMain.addView(main);
setContentView(scrollMain);

refreshContent();
}

private void initData() {
addCat("maths", "Mathématiques", null);
addCat("physique", "Physique", null);
addCat("algebre", "Algèbre", "maths");
addCat("analyse", "Analyse", "maths");
addCat("mecanique", "Mécanique", "physique");
addCat("optique", "Optique", "physique");
addDoc("d1", "Cours Algèbre Linéaire", "algebre", "PDF");
addDoc("d2", "Exercices Analyse", "analyse", "PDF");
addDoc("d3", "TD Mécanique Quantique", "mecanique", "PDF");
addDoc("d4", "Formulaire Optique", "optique", "PDF");
addDoc("d5", "Résumé Maths Sup", "maths", "PDF");
addDoc("d6", "Exam Physique 2024", "physique", "PDF");
totalDocs = documents.size();
}

private void addCat(String id, String name, String parent) {
Map<String,String> m = new HashMap<>();
m.put("id", id); m.put("name", name);
m.put("parent", parent != null ? parent : "");
categories.add(m);
}

private void addDoc(String id, String title, String catId, String type) {
Map<String,String> m = new HashMap<>();
m.put("id", id); m.put("title", title);
m.put("cat", catId); m.put("type", type);
documents.add(m);
}

private LinearLayout buildHeader() {
LinearLayout h = new LinearLayout(this);
h.setOrientation(LinearLayout.HORIZONTAL);
h.setPadding(10, 20, 10, 30);
h.setGravity(Gravity.CENTER_VERTICAL);

// Logo + Title
LinearLayout left = new LinearLayout(this);
left.setOrientation(LinearLayout.VERTICAL);
left.setOnClickListener(v -> { navStack.clear(); refreshContent(); });

TextView logo = new TextView(this);
logo.setText("⚛");
logo.setTextSize(28);
logo.setTextColor(0xFF00D4FF);
left.addView(logo);

headerTitle = new TextView(this);
headerTitle.setText("SuccessPolaris");
headerTitle.setTextSize(22);
headerTitle.setTextColor(0xFFFFFFFF);
headerTitle.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC);
left.addView(headerTitle);

h.addView(left, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

totalText = new TextView(this);
totalText.setText(totalDocs + " items");
totalText.setTextSize(10);
totalText.setTextColor(0xFF4A4A8A);
h.addView(totalText);

return h;
}

private LinearLayout buildSearch() {
LinearLayout sb = new LinearLayout(this);
sb.setPadding(0, 0, 0, 15);

searchInput = new EditText(this);
searchInput.setHint("Explorer avec Astarté...");
searchInput.setTextColor(0xFFFFFFFF);
searchInput.setHintTextColor(0x44FFFFFF);
searchInput.setBackground(createRoundBg(0x11FFFFFF, 40));
searchInput.setPadding(35, 28, 35, 28);
searchInput.addTextChangedListener(new TextWatcher() {
public void afterTextChanged(Editable s) { refreshContent(); }
public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
public void onTextChanged(CharSequence s, int st, int b, int c) {}
});
sb.addView(searchInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
return sb;
}

private LinearLayout buildFooter() {
LinearLayout f = new LinearLayout(this);
f.setOrientation(LinearLayout.HORIZONTAL);
f.setPadding(10, 20, 10, 10);
f.setGravity(Gravity.CENTER);

TextView ft = new TextView(this);
ft.setText("POLARIS PROTOCOL // Système Sécurisé");
ft.setTextSize(7);
ft.setTextColor(0xFF1A1A4A);
f.addView(ft);
return f;
}

private Drawable createRoundBg(int color, float radius) {
GradientDrawable gd = new GradientDrawable();
gd.setColor(color);
gd.setCornerRadius(radius);
return gd;
}

private void refreshContent() {
catList.removeAllViews();
docGrid.removeAllViews();
navPath.removeAllViews();

String query = searchInput.getText().toString().trim().toLowerCase();

// Nav path chips
for (int i = 0; i < navStack.size(); i++) {
final int idx = i;
String name = "";
for (Map<String,String> c : categories) {
if (c.get("id").equals(navStack.get(i))) { name = c.get("name"); break; }
}
TextView chip = new TextView(this);
chip.setText(name);
chip.setTextSize(9);
chip.setTextColor(0xFF00D4FF);
chip.setPadding(15, 10, 15, 10);
chip.setBackground(createRoundBg(0x1100D4FF, 30));
chip.setOnClickListener(v -> {
navStack = new ArrayList<>(navStack.subList(0, idx + 1));
viewMode = "archives";
refreshContent();
});
navPath.addView(chip);
}

if (query.isEmpty()) {
// View mode buttons
LinearLayout modes = new LinearLayout(this);
modes.setOrientation(LinearLayout.HORIZONTAL);
modes.setPadding(0, 10, 0, 15);

Button btnSecteurs = new Button(this);
btnSecteurs.setText("Secteurs");
styleModeButton(btnSecteurs, viewMode.equals("archives") && navStack.isEmpty());
btnSecteurs.setOnClickListener(v -> { viewMode = "archives"; navStack.clear(); refreshContent(); });
modes.addView(btnSecteurs);

Button btnIndex = new Button(this);
btnIndex.setText("Mon Index");
styleModeButton(btnIndex, viewMode.equals("library"));
btnIndex.setOnClickListener(v -> { viewMode = "library"; refreshContent(); });
modes.addView(btnIndex);
catList.addView(modes);

if (viewMode.equals("archives")) {
String parentId = navStack.isEmpty() ? "" : navStack.get(navStack.size() - 1);
List<Map<String,String>> subCats = new ArrayList<>();
for (Map<String,String> c : categories) {
String p = c.get("parent");
if (p == null) p = "";
if (p.equals(parentId)) subCats.add(c);
}
for (Map<String,String> c : subCats) {
catList.addView(buildCategoryButton(c));
}
// Docs pour la catégorie courante
if (!navStack.isEmpty()) {
for (Map<String,String> d : documents) {
if (d.get("cat").equals(navStack.get(navStack.size() - 1))) {
docGrid.addView(buildDocumentCard(d));
}
}
}
} else {
for (Map<String,String> d : documents) {
docGrid.addView(buildDocumentCard(d));
}
}
} else {
for (Map<String,String> d : documents) {
if (d.get("title").toLowerCase().contains(query)) {
docGrid.addView(buildDocumentCard(d));
}
}
}
}

private Button buildCategoryButton(final Map<String,String> cat) {
Button btn = new Button(this);
btn.setText(cat.get("name") + " →");
btn.setTextSize(12);
btn.setTextColor(0xFFFFFFFF);
btn.setBackground(createRoundBg(0x11FFFFFF, 50));
btn.setPadding(25, 20, 25, 20);
btn.setAllCaps(true);
btn.setOnClickListener(v -> {
navStack.add(cat.get("id"));
viewMode = "archives";
refreshContent();
});
LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
lp.setMargins(0, 0, 0, 10);
btn.setLayoutParams(lp);
return btn;
}

private CardView buildDocumentCard(final Map<String,String> doc) {
CardView card = new CardView(this);
card.setRadius(40);
card.setCardBackgroundColor(0x11FFFFFF);
card.setCardElevation(0);
card.setPadding(30, 25, 30, 25);

LinearLayout inner = new LinearLayout(this);
inner.setOrientation(LinearLayout.VERTICAL);

TextView title = new TextView(this);
title.setText(doc.get("title"));
title.setTextSize(14);
title.setTextColor(0xFFFFFFFF);
inner.addView(title);

TextView type = new TextView(this);
type.setText("Type: " + doc.get("type"));
type.setTextSize(10);
type.setTextColor(0xFF4A4A8A);
inner.addView(type);

LinearLayout btns = new LinearLayout(this);
btns.setOrientation(LinearLayout.HORIZONTAL);
btns.setPadding(0, 15, 0, 0);

Button preview = new Button(this);
preview.setText("APERÇU");
preview.setTextSize(9);
preview.setTextColor(0xFF00D4FF);
preview.setBackground(createRoundBg(0x1100D4FF, 20));
preview.setPadding(20, 10, 20, 10);
preview.setOnClickListener(v -> showPreview(doc));
btns.addView(preview);

Button download = new Button(this);
download.setText("OBTENIR");
download.setTextSize(9);
download.setTextColor(0xFF000000);
download.setBackground(createRoundBg(0xFF00D4FF, 20));
download.setPadding(20, 10, 20, 10);
download.setOnClickListener(v -> showEmailModal(doc));
btns.addView(download);

inner.addView(btns);
card.addView(inner);

LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
lp.setMargins(0, 0, 0, 15);
card.setLayoutParams(lp);
return card;
}

private void showPreview(Map<String,String> doc) {
AlertDialog.Builder b = new AlertDialog.Builder(this);
b.setTitle(doc.get("title"));
b.setMessage("Aperçu du document " + doc.get("title") + "\nType: " + doc.get("type") + "\n\nContenu simulé pour l'APK native.");
b.setPositiveButton("Fermer", null);
b.show();
}

private void showEmailModal(final Map<String,String> doc) {
AlertDialog.Builder b = new AlertDialog.Builder(this);
b.setTitle("Initialisation du Canal");
b.setMessage("Confirmez votre identité pour accéder.");

LinearLayout ll = new LinearLayout(this);
ll.setOrientation(LinearLayout.VERTICAL);
ll.setPadding(40, 20, 40, 20);

final EditText emailInput = new EditText(this);
emailInput.setHint("votre-id@gmail.com");
emailInput.setText(email);
ll.addView(emailInput);

final Spinner countrySpinner = new Spinner(this);
String[] countries = {"Togo","Bénin","Côte d'Ivoire","Burkina Faso","Sénégal","Mali","Niger","France"};
ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
countrySpinner.setAdapter(adapter);
ll.addView(countrySpinner);

b.setView(ll);
b.setPositiveButton("Accéder", (dialog, which) -> {
email = emailInput.getText().toString().trim();
country = countrySpinner.getSelectedItem().toString();
if (email.contains("@")) {
Toast.makeText(this, "Accès autorisé: " + doc.get("title"), Toast.LENGTH_SHORT).show();
} else {
Toast.makeText(this, "Email invalide", Toast.LENGTH_SHORT).show();
}
});
b.setNegativeButton("Annuler", null);
b.show();
}

private void styleModeButton(Button btn, boolean active) {
btn.setTextSize(10);
btn.setPadding(25, 15, 25, 15);
btn.setAllCaps(true);
if (active) {
btn.setTextColor(0xFF000000);
btn.setBackground(createRoundBg(0xFF00D4FF, 30));
} else {
btn.setTextColor(0x88FFFFFF);
btn.setBackground(createRoundBg(0x11FFFFFF, 30));
}
}
}