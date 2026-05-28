package com.example.n_proyect.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.n_proyect.Clases.Carrito;
import com.example.n_proyect.Clases.DetallePedido;
import com.example.n_proyect.Clases.Pedido;
import com.example.n_proyect.Clases.Producto;
import com.example.n_proyect.Clases.ProductoVideo;
import com.example.n_proyect.Clases.StockExistencias;
import com.example.n_proyect.Clases.StockProducto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GestorN_Proyect extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "n_proyect.db";
    private static final int VERSION_BD = 2;
    private Context contexto;
    private static String ubicacionBaseDatos = "";

    public GestorN_Proyect(Context contexto) {
        super(contexto, NOMBRE_BD, null, VERSION_BD);
        this.contexto = contexto;
        ubicacionBaseDatos = contexto.getDatabasePath(NOMBRE_BD).getPath();
        copiarBaseDeDatos();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // codigo tablas para crear
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // actualizar bd
    }

    private void copiarBaseDeDatos() {
        File dbArchivo = new File(ubicacionBaseDatos);
        if (!dbArchivo.exists()) {
            getReadableDatabase();

            try {
                InputStream input = contexto.getAssets().open(NOMBRE_BD);
                OutputStream output = new FileOutputStream(ubicacionBaseDatos);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al copiar la base de datos desde assets: " + e.getMessage());
            }
        }
    }

    // insertar codigo
    public boolean insertarUsuario(String correo, String contraseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("correo", correo);
        values.put("contraseña", contraseña);
        values.put("rol", "cliente"); // rol cliente por defecto

        long resultado = db.insert("usuarios", null, values);
        return resultado != -1;
    }

    // verificar usuario
    public boolean verificarUsuario(String correo, String contraseña) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?", new String[]{correo, contraseña});
        boolean usuarioValido = cursor.getCount() > 0;
        cursor.close();
        return usuarioValido;
    }

    // obtener rol
    public String obtenerRol(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT rol FROM usuarios WHERE correo = ?", new String[]{correo});

        String rol = null;
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("rol");
            if (index != -1) {
                rol = cursor.getString(index);
            }
        }
        cursor.close();
        return rol;
    }

    public Cursor obtenerTodosUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, correo, rol FROM usuarios", null);
    }

    // coger id del usuario
    public Cursor obtenerUsuarioPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT id, correo, rol, telefono, direccion FROM usuarios WHERE id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // actualizar rol de usuario
    public boolean actualizarRolUsuario(int id, String nuevoRol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("rol", nuevoRol);

        int filasAfectadas = db.update("usuarios", valores, "id = ?",
                new String[]{String.valueOf(id)});
        return filasAfectadas > 0;
    }

    // eliminar usuario
    public boolean eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete("usuarios", "id = ?",
                new String[]{String.valueOf(id)});
        return filasAfectadas > 0;
    }
    // obtener usuario por usuario
    public Cursor obtenerUsuarioPorCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, correo, rol, telefono, direccion FROM usuarios WHERE correo = ?",
                new String[]{correo});
    }

    // actualizar correo
    public boolean actualizarCorreoUsuario(int id, String nuevoCorreo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("correo", nuevoCorreo);

        int filasAfectadas = db.update("usuarios", valores, "id = ?",
                new String[]{String.valueOf(id)});
        return filasAfectadas > 0;
    }

    // actualizar contraseña
    public boolean actualizarContraseñaUsuario(int id, String nuevaContraseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("contraseña", nuevaContraseña);

        int filasAfectadas = db.update("usuarios", valores, "id = ?",
                new String[]{String.valueOf(id)});
        return filasAfectadas > 0;
    }

    // id por correo
    public int obtenerIdUsuarioPorCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE correo = ?", new String[]{correo});

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    // verificar correo
    public boolean existeCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE correo = ?", new String[]{correo});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    // actualizar telefono
    public boolean actualizarTelefonoUsuario(int id, String nuevoTelefono) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("telefono", nuevoTelefono);

        int filasAfectadas = db.update("usuarios", valores, "id = ?",
                new String[]{String.valueOf(id)});
        return filasAfectadas > 0;
    }

    // actualizar dirrecion
    public boolean actualizarDireccionUsuario(int id, String nuevaDireccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("direccion", nuevaDireccion);

        int filasAfectadas = db.update("usuarios", valores, "id = ?",
                new String[]{String.valueOf(id)});
        return filasAfectadas > 0;
    }

    // ==============================================================
    //  obtener productos por categoria y genero
    // ==============================================================
    public List<Producto> obtenerProductosCamisetasHombre() {
        return filtrarProductos("Todas", "");
    }


    public List<Producto> obtenerProductosCamisetasMujer() {
        return filtrarProductosPorCategoria("Camisetas", "mujer", "Todas", "");
    }

    public List<Producto> obtenerProductosSudaderasHombre() {
        return filtrarProductosPorCategoria("Sudaderas", "hombre", "Todas", "");
    }
    public List<Producto> obtenerProductosSudaderasMujer() {
        return filtrarProductosPorCategoria("Sudaderas", "mujer", "Todas", "");
    }
    public List<Producto> obtenerProductosAcessoriosMujer() {
        return filtrarProductosPorCategoria("Acessorios", "mujer", "Todas", "");
    }
    public List<Producto> obtenerProductosAbrigosMujer() {
        return filtrarProductosPorCategoria("Abrigos", "mujer", "Todas", "");
    }

    public List<Producto> obtenerProductosPolosHombre() {
        return filtrarProductosPorCategoria("Polos", "hombre", "Todas", "");
    }

    public List<Producto> obtenerProductosPantalonesHombre() {
        return filtrarProductosPorCategoria("Pantalones", "hombre", "Todas", "");
    }
    public List<Producto> obtenerProductosPantalonesMujer() {
        return filtrarProductosPorCategoria("Pantalones", "mujer", "Todas", "");
    }
    public List<Producto> obtenerProductosPuntoMujer() {
        return filtrarProductosPorCategoria("Punto", "mujer", "Todas", "");
    }

    public List<Producto> obtenerProductosAbrigosHombre() {
        return filtrarProductosPorCategoria("Abrigos", "hombre", "Todas", "");
    }

    // ==============================================================
    //  fin obtener productos por categoria y genero
    // ==============================================================


    // filtar prod por categoria
    public List<Producto> filtrarProductosPorCategoria(String categoria, String genero, String talla, String query) {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL optimizada que considera el stock disponible real
        StringBuilder sql = new StringBuilder()
                .append("SELECT DISTINCT p.id, p.nombre, p.descripcion, p.precio, ")
                .append("p.imagen_principal, p.imagen_secundaria ")
                .append("FROM productos p ")
                .append("JOIN categorias c ON p.categoria_id = c.id ")
                .append("JOIN inventario i ON p.id = i.producto_id ")
                .append("WHERE UPPER(c.nombre) = UPPER(?) ")  // Insensible a mayúsculas
                .append("AND UPPER(c.genero) = UPPER(?) ")
                .append("AND i.cantidad > 0 ");

        List<String> parametros = new ArrayList<>();
        parametros.add(categoria);
        parametros.add(genero);

        // Manejo mejorado del filtro de talla
        if (talla != null && !talla.equalsIgnoreCase("Todas")) {
            sql.append("AND UPPER(i.talla) = UPPER(?) ");  // Insensible a mayúsculas
            parametros.add(talla);
        }

        // Manejo del texto de búsqueda
        if (query != null && !query.isEmpty()) {
            sql.append("AND (p.nombre LIKE ? OR p.descripcion LIKE ?) ");
            parametros.add("%" + query + "%");
            parametros.add("%" + query + "%");
        }

        try (Cursor cursor = db.rawQuery(sql.toString(), parametros.toArray(new String[0]))) {
            int idIndex = cursor.getColumnIndex("id");
            int nombreIndex = cursor.getColumnIndex("nombre");
            int descIndex = cursor.getColumnIndex("descripcion");
            int precioIndex = cursor.getColumnIndex("precio");
            int imgPrincipalIndex = cursor.getColumnIndex("imagen_principal");
            int imgSecundariaIndex = cursor.getColumnIndex("imagen_secundaria");

            while (cursor.moveToNext()) {
                listaProductos.add(new Producto(
                        cursor.getInt(idIndex),
                        cursor.getString(nombreIndex),
                        cursor.getString(descIndex),
                        cursor.getDouble(precioIndex),
                        cursor.getString(imgPrincipalIndex),
                        cursor.getString(imgSecundariaIndex)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return listaProductos;
    }


    // filtar prod
    public List<Producto> filtrarProductos(String talla, String query) {
        List<Producto> listaProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder sql = new StringBuilder()
                .append("SELECT DISTINCT p.id, p.nombre, p.descripcion, p.precio, p.imagen_principal, p.imagen_secundaria ")
                .append("FROM productos p ")
                .append("JOIN categorias c ON p.categoria_id = c.id ")
                .append("JOIN inventario i ON p.id = i.producto_id ")
                .append("WHERE c.nombre = 'Camisetas' ")
                .append("AND c.genero = 'hombre' ")
                .append("AND i.cantidad > 0 ");

        List<String> parametros = new ArrayList<>();

        if (talla != null && !talla.equalsIgnoreCase("Todas")) {
            sql.append("AND i.talla = ? ");
            parametros.add(talla);
        }

        if (query != null && !query.isEmpty()) {
            sql.append("AND (p.nombre LIKE ? OR p.descripcion LIKE ?) ");
            parametros.add("%" + query + "%");
            parametros.add("%" + query + "%");
        }

        try (Cursor cursor = db.rawQuery(sql.toString(), parametros.toArray(new String[0]))) {
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex("id");
                    int nombreIndex = cursor.getColumnIndex("nombre");
                    int descIndex = cursor.getColumnIndex("descripcion");
                    int precioIndex = cursor.getColumnIndex("precio");
                    int imgPrincipalIndex = cursor.getColumnIndex("imagen_principal");
                    int imgSecundariaIndex = cursor.getColumnIndex("imagen_secundaria");

                    listaProductos.add(new Producto(
                            cursor.getInt(idIndex),
                            cursor.getString(nombreIndex),
                            cursor.getString(descIndex),
                            cursor.getDouble(precioIndex),
                            cursor.getString(imgPrincipalIndex),
                            cursor.getString(imgSecundariaIndex) // Nuevo campo
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return listaProductos;
    }


    // obtener productos
    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, descripcion, precio, imagen_principal, imagen_secundaria FROM productos",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                productos.add(new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5) // Imagen secundaria
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productos;
    }

    // actualizar prod
    public boolean actualizarProducto(int id, String nombre, String descripcion, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("precio", precio);
        return db.update("productos", values, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // eliminar produc
    public boolean eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("productos", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }


    // obtener prod id
    public Producto obtenerProductoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, descripcion, precio, imagen_principal, imagen_secundaria " +
                        "FROM productos WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Producto producto = null;
        if (cursor.moveToFirst()) {
            producto = new Producto(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                    cursor.getString(cursor.getColumnIndexOrThrow("imagen_principal")),
                    cursor.getString(cursor.getColumnIndexOrThrow("imagen_secundaria"))
            );
        }
        cursor.close();
        return producto;
    }

    // agregar  productos al carrito
    public int agregarAlCarrito(int usuarioId, int productoId, String talla, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            Cursor disponibilidadCursor = db.rawQuery(
                    "SELECT i.cantidad - COALESCE((SELECT SUM(c.cantidad) FROM carrito c " +
                            "WHERE c.producto_id = i.producto_id AND c.talla = i.talla), 0) as disponible " +
                            "FROM inventario i " +
                            "WHERE i.producto_id = ? AND i.talla = ?",
                    new String[]{String.valueOf(productoId), talla}
            );

            int disponible = 0;
            if (disponibilidadCursor.moveToFirst()) {
                disponible = disponibilidadCursor.getInt(0);
            }
            disponibilidadCursor.close();

            if (disponible < cantidad) {
                db.endTransaction();
                return -1;
            }

            ContentValues values = new ContentValues();
            Cursor cartCursor = db.rawQuery(
                    "SELECT id, cantidad FROM carrito " +
                            "WHERE usuario_id = ? AND producto_id = ? AND talla = ?",
                    new String[]{String.valueOf(usuarioId), String.valueOf(productoId), talla}
            );

            if (cartCursor.moveToFirst()) {
                int existingId = cartCursor.getInt(0);
                int newQuantity = cartCursor.getInt(1) + cantidad;
                values.put("cantidad", newQuantity);
                db.update("carrito", values, "id = ?", new String[]{String.valueOf(existingId)});
            } else {
                values.put("usuario_id", usuarioId);
                values.put("producto_id", productoId);
                values.put("talla", talla);
                values.put("cantidad", cantidad);
                db.insert("carrito", null, values);
            }
            cartCursor.close();

            db.setTransactionSuccessful();
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // obtener carrito
    public List<Carrito> obtenerCarrito(int usuarioId) {
        List<Carrito> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.id, c.producto_id, p.nombre, p.imagen_principal, " +
                "c.talla, c.cantidad, p.precio " +
                "FROM carrito c " +
                "JOIN productos p ON c.producto_id = p.id " +
                "WHERE c.usuario_id = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)})) {
            while (cursor.moveToNext()) {
                items.add(new Carrito(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getDouble(6)
                ));
            }
        }
        return items;
    }

    // eliminar prodic
    public boolean eliminarDelCarrito(int carritoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;
        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(
                    "SELECT producto_id, talla, cantidad FROM carrito WHERE id = ?",
                    new String[]{String.valueOf(carritoId)}
            );

            if (cursor.moveToFirst()) {
                int productoId = cursor.getInt(0);
                String talla = cursor.getString(1);
                int cantidad = cursor.getInt(2);

                // restaurar stock
                db.execSQL("UPDATE inventario SET cantidad = cantidad + ? WHERE producto_id = ? AND talla = ?",
                        new Object[]{cantidad, productoId, talla});

                // eliminar del carrito
                success = db.delete("carrito", "id = ?",
                        new String[]{String.valueOf(carritoId)}) > 0;
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return success;
    }

    // crear pedido
    public boolean crearPedido(int usuarioId, double total, String direccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            // 1. crear el pedido
            ContentValues values = new ContentValues();
            values.put("usuario_id", usuarioId);
            values.put("total", total);
            values.put("direccion_envio", direccion);
            values.put("metodo_pago", "Tarjeta");
            values.put("estado", "pendiente");

            long pedidoId = db.insertOrThrow("pedidos", null, values);
            if(pedidoId == -1) return false;


            List<Carrito> items = obtenerCarrito(usuarioId);
            for(Carrito item : items) {
                // insertar detalle del pedido
                ContentValues detalle = new ContentValues();
                detalle.put("pedido_id", pedidoId);
                detalle.put("producto_id", item.getProductoId());
                detalle.put("talla", item.getTalla());
                detalle.put("cantidad", item.getCantidad());
                detalle.put("precio_unitario", item.getPrecioUnitario());
                db.insert("detalles_pedido", null, detalle);

                // restar del inventario
                db.execSQL(
                        "UPDATE inventario SET cantidad = cantidad - ? " +
                                "WHERE producto_id = ? AND talla = ?",
                        new Object[]{item.getCantidad(), item.getProductoId(), item.getTalla()}
                );
            }

            // 3. vaciar carrito
            db.delete("carrito", "usuario_id = ?", new String[]{String.valueOf(usuarioId)});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }


    // obtener videos
    public List<ProductoVideo> obtenerVideosProductos() {
        List<ProductoVideo> videos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT producto_id, nombre_video FROM videos_productos",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                videos.add(new ProductoVideo(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }

    // obtener stock
    public List<StockProducto> obtenerStockProductos() {
        List<StockProducto> stockProductos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p.id, p.nombre, p.descripcion, i.talla, i.cantidad " +
                "FROM productos p " +
                "JOIN inventario i ON p.id = i.producto_id " +
                "ORDER BY p.nombre, i.talla";

        try (Cursor cursor = db.rawQuery(query, null)) {
            int currentProductId = -1;
            StockProducto currentProduct = null;
            List<StockExistencias> stockItems = new ArrayList<>();

            while (cursor.moveToNext()) {
                int productId = cursor.getInt(0);
                if (productId != currentProductId) {
                    if (currentProduct != null) {
                        stockProductos.add(new StockProducto(
                                currentProductId,
                                currentProduct.getNombre(),
                                currentProduct.getDescripcion(),
                                new ArrayList<>(stockItems)
                        ));
                        stockItems.clear();
                    }
                    currentProductId = productId;
                    currentProduct = new StockProducto(
                            productId,
                            cursor.getString(1),
                            cursor.getString(2),
                            new ArrayList<>()
                    );
                }
                stockItems.add(new StockExistencias(
                        cursor.getString(3),
                        cursor.getInt(4)
                ));
            }

            if (currentProduct != null) {
                stockProductos.add(new StockProducto(
                        currentProductId,
                        currentProduct.getNombre(),
                        currentProduct.getDescripcion(),
                        new ArrayList<>(stockItems)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockProductos;
    }


    // actualizar inventario
    public boolean actualizarInventario(int productoId, String talla, int nuevaCantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cantidad", nuevaCantidad);
        try {
            int rows = db.update("inventario", values,
                    "producto_id = ? AND talla = ?",
                    new String[]{String.valueOf(productoId), talla});
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // restaurar stock
    public boolean restaurarStock(int pedidoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            Cursor cursor = db.rawQuery(
                    "SELECT producto_id, talla, cantidad FROM detalles_pedido WHERE pedido_id = ?",
                    new String[]{String.valueOf(pedidoId)}
            );

            while (cursor.moveToNext()) {
                int productoId = cursor.getInt(0);
                String talla = cursor.getString(1);
                int cantidad = cursor.getInt(2);

                db.execSQL(
                        "UPDATE inventario SET cantidad = cantidad + ? " +
                                "WHERE producto_id = ? AND talla = ?",
                        new Object[]{cantidad, productoId, talla}
                );
            }
            cursor.close();

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    // obtener total prod mujer
    public int obtenerTotalProductosMujer() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(DISTINCT p.id) FROM productos p " +
                "JOIN categorias c ON p.categoria_id = c.id " +
                "WHERE c.genero = 'mujer'";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // obtener total prod hombre
    public int obtenerTotalProductosHombre() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(DISTINCT p.id) FROM productos p " +
                "JOIN categorias c ON p.categoria_id = c.id " +
                "WHERE c.genero = 'hombre'";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // obtener pedidos de hoy
    public int obtenerPedidosHoy() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM pedidos WHERE date(fecha_pedido) = date('now')";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    // obtener stock por producto
    public List<StockExistencias> obtenerStockPorProducto(int productoId) {
        List<StockExistencias> stockItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT i.talla, " +
                "(i.cantidad - COALESCE((SELECT SUM(c.cantidad) FROM carrito c " +
                "WHERE c.producto_id = i.producto_id AND c.talla = i.talla), 0)) AS disponible " +
                "FROM inventario i WHERE i.producto_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productoId)});

        while (cursor.moveToNext()) {
            String talla = cursor.getString(0);
            int disponible = cursor.getInt(1);
            stockItems.add(new StockExistencias(talla, disponible));
        }
        cursor.close();
        return stockItems;
    }

    // obtener tdos los pedidos
    public List<Pedido> obtenerTodosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p.id, p.usuario_id, p.fecha_pedido, p.estado, " +
                "p.direccion_envio, p.metodo_pago, p.total, u.correo " +
                "FROM pedidos p " +
                "JOIN usuarios u ON p.usuario_id = u.id " +
                "ORDER BY p.fecha_pedido DESC";

        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                Pedido pedido = new Pedido(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getDouble(6),
                        cursor.getString(7)
                );
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }




    // actualizar pedido
    public boolean actualizarPedido(Pedido pedido) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("estado", pedido.getEstado());
        values.put("direccion_envio", pedido.getDireccionEnvio());

        int rowsAffected = db.update("pedidos", values, "id = ?",
                new String[]{String.valueOf(pedido.getId())});

        return rowsAffected > 0;
    }


    // eliminar pedido
    public boolean eliminarPedido(int idPedido) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            db.beginTransaction();

            // restaurar stock primero
            List<DetallePedido> detalles = obtenerDetallesPedido(idPedido);
            for (DetallePedido detalle : detalles) {
                db.execSQL("UPDATE inventario SET cantidad = cantidad + ? " +
                                "WHERE producto_id = ? AND talla = ?",
                        new Object[]{detalle.getCantidad(), detalle.getProductoId(), detalle.getTalla()});
            }

            // eliminar detalles del pedido
            db.delete("detalles_pedido", "pedido_id = ?",
                    new String[]{String.valueOf(idPedido)});

            // eliminar el pedido
            int rowsAffected = db.delete("pedidos", "id = ?",
                    new String[]{String.valueOf(idPedido)});

            success = rowsAffected > 0;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return success;
    }

    // obtener detalles del pedido
    private List<DetallePedido> obtenerDetallesPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT producto_id, talla, cantidad " +
                        "FROM detalles_pedido WHERE pedido_id = ?",
                new String[]{String.valueOf(idPedido)})) {

            while (cursor.moveToNext()) {
                detalles.add(new DetallePedido(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2)
                ));
            }
        }
        return detalles;
    }


    // obtener compras por usuario
    public Cursor obtenerComprasPorUsuario(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                "pedidos",
                new String[]{"id AS _id", "fecha_pedido", "total", "estado"},
                "usuario_id = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                "fecha_pedido DESC"
        );
    }


    // obtener detalles de compra
    public Cursor obtenerDetallesCompra(int idPedido) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT p.nombre AS nombre_producto, " +
                        "dp.cantidad, " +
                        "dp.precio_unitario, " +
                        "p.imagen_principal AS imagen_producto " +
                        "FROM detalles_pedido dp " +
                        "JOIN productos p ON dp.producto_id = p.id " +
                        "WHERE dp.pedido_id = ?",
                new String[]{String.valueOf(idPedido)}
        );
    }

    // metodo de pago
    public boolean insertarMetodoPago(int usuarioId, String tipo, String ultimosDigitos,
                                      String fechaVencimiento, String cvv, String telefono) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario_id", usuarioId);
        values.put("tipo", tipo);

        if(tipo.equals("tarjeta")) {
            values.put("ultimos_digitos", ultimosDigitos);
            values.put("fecha_vencimiento", fechaVencimiento);
        } else {
            values.put("telefono_bizum", telefono);
        }

        long resultado = db.insert("metodos_pago", null, values);
        return resultado != -1;
    }


    public void vaciarCarritoPorUsuario(String correoUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        int usuarioId = obtenerIdUsuarioPorCorreo(correoUsuario);
        db.delete("carrito", "usuario_id = ?", new String[]{String.valueOf(usuarioId)});
    }





}
