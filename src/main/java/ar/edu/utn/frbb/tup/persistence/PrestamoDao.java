package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.persistence.entity.PrestamoEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class PrestamoDao extends AbstractBaseDao {

    @Override
    protected String getEntityName() {
        return "PRESTAMO";
    }

    private long nextId = 1;

    public void save(Prestamo prestamo) {
        if (prestamo.getId() == 0) {
            prestamo.setId(nextId++);
        }
        PrestamoEntity entity = new PrestamoEntity(prestamo);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Prestamo find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((PrestamoEntity) getInMemoryDatabase().get(id)).toPrestamo();
    }

    public List<Prestamo> getPrestamosByCliente(long numeroCliente) {
        List<Prestamo> prestamosDelCliente = new ArrayList<>();
        for (Object object : 
                getInMemoryDatabase().values()) {
            PrestamoEntity prestamoEntity = ((PrestamoEntity) object);
            if (prestamoEntity.getNumeroCliente() == numeroCliente) {
                prestamosDelCliente.add(prestamoEntity.toPrestamo());
            }
        }
        return prestamosDelCliente;
    }
}


