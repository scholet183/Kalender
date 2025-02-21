export interface Appointment {
  id?: number;       // Optional, da es beim Erstellen noch nicht vorhanden ist
  userId: number;
  title: string;
  startDate: string; // ISO 8601 Datumsformat (z.B. "2025-02-20T09:00:00Z")
  endDate: string;   // ISO 8601 Datumsformat
  description?: string;
  location?: string;
}

