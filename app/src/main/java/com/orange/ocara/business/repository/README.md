# package "repository"
  
This package contains all the interfaces that describe how repositories shall share data from the *data* layer to the *domain* layer.

These interfaces should be named with the kind of data (ie Audit, Equipments, ...) and suffixed as **-Repository**. Examples : AuditRepository, EquipmentRepository.   