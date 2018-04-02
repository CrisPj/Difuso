package com.pythonteam.archivos;

import com.pythonteam.arbol.Elemento;
import com.pythonteam.arbol.Regla;
import com.pythonteam.common.Constantes;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ArchivoReglas
{

    private RandomAccessFile file;
    private ArrayList<Regla> reglas;

    public ArchivoReglas(String nombre, String permisos) {
        reglas = new ArrayList<>();
        try {
            file = new RandomAccessFile(nombre + Constantes.EXTENCION_REGLAS, permisos);
            if (file.length() > 0)
                readFile();
        } catch (Exception ex) {
            System.out.println("Archivo no pudo ser creado");
        }
    }

    public boolean isEmpty() throws IOException {
        return file.length() == 0;
    }

    private void readFile() {
        try {
            file.seek(0);
            for (int x = 0; x < file.length(); x++) {
                Regla r = new Regla();
                r.setId(file.readInt());
                ArrayList<Elemento> elementos = new ArrayList<>();
                byte[] registroActual = new byte[Constantes.TAM_REGISTRO];
                int z = file.readInt();
                for (int i = 0; i < z; i++) {

                    Elemento e = new Elemento();
                    e.setIdAlias(file.readInt());
                    file.read(registroActual);
                    e.setAlias(new String(registroActual).trim());
                    e.setIdFuncion(file.readInt());
                    file.read(registroActual);
                    e.setFuncion(new String(registroActual).trim());
                    e.setValorDifuso(file.readDouble());
                    elementos.add(e);
                }
                r.setAntecedentes(elementos);
                Elemento e = new Elemento();
                file.read(registroActual);
                e.setAlias(new String(registroActual).trim());
                file.read(registroActual);
                e.setFuncion(new String(registroActual).trim());
                e.setValorDifuso(file.readDouble());
                r.setConsecuente(e);
                reglas.add(r);
            }
        } catch (IOException e) {
            System.out.println("Se ha leido el archivo de indices por completo");
        }
    }


    public boolean editarRegla(Regla regla) {
        Regla r = reglas.get(regla.getId());
        r.setId(regla.getId());
        r.setAntecedentes(regla.getAntecedentes());
        r.setConsecuente(regla.getConsecuente());
        writeFile();
        return true;
    }

    void escribir(Regla r) throws IOException {
        StringBuffer buffer;
        file.writeInt(r.getId());
            file.writeInt(r.getAntecedentes().size());
            for (Elemento e:r.getAntecedentes()) {
                file.writeInt(e.getIdAlias());
                buffer = new StringBuffer(e.getAlias());
                buffer.setLength(Constantes.TAM_REGISTRO);
                file.writeChars(buffer.toString());

                file.writeInt(e.getIdFuncion());
                buffer = new StringBuffer(e.getFuncion());
                buffer.setLength(Constantes.TAM_REGISTRO);
                file.writeChars(buffer.toString());
                if (e.getValorDifuso()==null)
                    e.setValorDifuso(0.0);
                file.writeDouble(e.getValorDifuso());
            }
            Elemento e = r.getConsecuente();
            if (e == null)
                e = new Elemento();
            if (e.getAlias() == null)
                e.setAlias("");

            file.writeInt(e.getIdAlias());
            buffer = new StringBuffer(e.getAlias());
            buffer.setLength(Constantes.TAM_REGISTRO);
            file.writeChars(buffer.toString());
            if (e.getFuncion()== null)
                e.setFuncion("");
            file.writeInt(e.getIdFuncion());
            buffer = new StringBuffer(e.getFuncion());
            buffer.setLength(Constantes.TAM_REGISTRO);
            file.writeChars(buffer.toString());
            if (e.getValorDifuso() == null)
                e.setValorDifuso(0.0);
            file.writeDouble(e.getValorDifuso());
    }

    public void insertarRegla(Regla r) {
        reglas.add(r);
        try{
            escribir(r);
        } catch (Exception ex) {
            System.out.println("No se pudo insertar Regla");
        }
    }

    public ArrayList<Regla> obtenerReglas() {
        return reglas;
    }

    public void writeFile()
    {
        borrarReglas();
        try {
            file.seek(0);
            for (Regla r:reglas) {
                escribir(r);
            }
        } catch (Exception e)
        {
            System.out.println("Fallor al escribir la regla");
        }
    }

    public void borrarHecho(int id)
    {
        reglas.removeIf(e -> e.getId()==id);
    }

    public void borrarReglas() {
        reglas.clear();
        try {
            file.setLength(0);
        } catch (Exception ex) {
            System.out.println("reglas no pudieron ser eliminados");
        }
    }

    public Regla obtenerRegla(int id) {
        return reglas.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    public double getMax() {
        double max = -1;
        for (Regla r : reglas) {
            ArrayList<Elemento> antecedentes = r.getAntecedentes();
            for (Elemento e : antecedentes) {
                if (max < e.getValorDifuso()) max = e.getValorDifuso();
            }
        }
        return max;
    }
}
