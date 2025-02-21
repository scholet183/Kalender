const express = require('express');
const bodyParser = require('body-parser');
const mariadb = require('mariadb');
const cors = require('cors');

const app = express();
const port = 3000;

app.use(bodyParser.json());
app.use(cors());

const pool = mariadb.createPool({
  host: 'localhost',
  user: 'root', // Dein MariaDB-Benutzer
  password: 'pack_it_up', // Dein MariaDB-Passwort
  database: 'kalender'
});

// Endpunkt zum Hinzufügen eines Termins
app.post('/api/termine', async (req, res) => {
  const { titel, datum, beschreibung } = req.body;
  try {
    const conn = await pool.getConnection();
    const result = await conn.query(
      'INSERT INTO termine (titel, datum, beschreibung) VALUES (?, ?, ?)',
      [titel, datum, beschreibung]
    );
    res.json({ message: 'Termin erfolgreich gespeichert', id: result.insertId });
    conn.release();
  } catch (err) {
    res.status(500).json({ error: 'Fehler beim Speichern des Termins' });
  }
});

app.listen(port, () => {
  console.log(`Server läuft auf http://localhost:${port}`);
});
