import React, { useState, useEffect, useMemo } from 'react';
import api from '../services/api';

// --- Reusable Icon Components for Actions ---
// Why: สร้าง SVG Icons เป็น component ของตัวเองเพื่อให้โค้ดในส่วน JSX สะอาดขึ้น
const EditIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>;
const DeleteIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>;
const AddIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4" /></svg>;


// --- Reusable TenantModal Component ---
const TenantModal = ({ isOpen, onClose, onSave, availableUnits, tenantData, setTenantData, isEditing }) => {
  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTenantData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave();
  };
  
  const formatDateForInput = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toISOString().split('T')[0];
  };

  return (
    <>
      <div className="fixed inset-0 z-40 bg-black bg-opacity-50" onClick={onClose}></div>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-lg shadow-xl w-full max-w-lg">
          <div className="p-5 border-b flex justify-between items-center">
            <h3 className="text-xl font-bold text-slate-800">{isEditing ? 'แก้ไขข้อมูลผู้เช่า' : 'เพิ่มผู้เช่าใหม่'}</h3>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-2xl">&times;</button>
          </div>
          <form onSubmit={handleSubmit}>
            <div className="p-6 space-y-4 max-h-[70vh] overflow-y-auto">
              <div>
                <label htmlFor="unitId" className="block text-sm font-medium text-gray-700">ห้องพัก</label>
                <select id="unitId" name="unitId" value={tenantData.unitId} onChange={handleChange} required className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm">
                  <option value="">-- กรุณาเลือกห้อง --</option>
                  {availableUnits.map(unit => ( <option key={unit.id} value={unit.id}>{unit.roomNumber}</option> ))}
                </select>
              </div>
              <div>
                <label htmlFor="name" className="block text-sm font-medium text-gray-700">ชื่อ-นามสกุล</label>
                <input type="text" id="name" name="name" value={tenantData.name} onChange={handleChange} required className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm" />
              </div>
               <div className="grid md:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">เบอร์โทรศัพท์</label>
                  <input type="tel" id="phoneNumber" name="phoneNumber" value={tenantData.phoneNumber} onChange={handleChange} className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm" />
                </div>
                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-gray-700">อีเมล</label>
                  <input type="email" id="email" name="email" value={tenantData.email} onChange={handleChange} className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm" />
                </div>
              </div>
              <div className="grid md:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="leaseStartDate" className="block text-sm font-medium text-gray-700">วันเริ่มสัญญา</label>
                  <input type="date" id="leaseStartDate" name="leaseStartDate" value={formatDateForInput(tenantData.leaseStartDate)} onChange={handleChange} required className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm" />
                </div>
                <div>
                  <label htmlFor="leaseEndDate" className="block text-sm font-medium text-gray-700">วันสิ้นสุดสัญญา</label>
                  <input type="date" id="leaseEndDate" name="leaseEndDate" value={formatDateForInput(tenantData.leaseEndDate)} onChange={handleChange} required className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm" />
                </div>
              </div>
            </div>
            <div className="px-6 py-4 bg-gray-50 flex justify-end space-x-4">
              <button type="button" onClick={onClose} className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300">ยกเลิก</button>
              <button type="submit" className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700">บันทึก</button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};


// --- Main TenantPage Component ---
function TenantPage() {
  const [tenants, setTenants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [availableUnits, setAvailableUnits] = useState([]);
  const [editingTenant, setEditingTenant] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  
  const initialTenantState = { name: '', phoneNumber: '', email: '', leaseStartDate: '', leaseEndDate: '', unitId: '' };
  const [tenantFormData, setTenantFormData] = useState(initialTenantState);

  const fetchTenants = async () => {
    try {
      setLoading(true);
      const response = await api.get('/tenants');
      setTenants(response.data);
    } catch (err) {
      setError('ไม่สามารถโหลดข้อมูลผู้เช่าได้');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTenants();
  }, []);

  const filteredTenants = useMemo(() => {
    if (!searchTerm) return tenants;
    return tenants.filter(tenant =>
      tenant.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      tenant.roomNumber.includes(searchTerm)
    );
  }, [tenants, searchTerm]);

  const handleOpenCreateModal = async () => {
    setEditingTenant(null);
    setTenantFormData(initialTenantState);
    try {
      const res = await api.get('/units');
      setAvailableUnits(res.data.filter(u => u.status === 'AVAILABLE'));
      setIsModalOpen(true);
    } catch (err) { setError('ไม่สามารถโหลดข้อมูลห้องว่างได้'); }
  };
  
  const handleOpenEditModal = async (tenant) => {
    setEditingTenant(tenant);
    
    // We need to find the original unitId from the unit object's tenant relationship
    // This is a bit complex due to our DTO structure, so we fetch all units to be sure
    try {
      const unitsRes = await api.get('/units');
      const currentUnit = unitsRes.data.find(u => u.tenant?.id === tenant.id);
      const otherAvailableUnits = unitsRes.data.filter(u => u.status === 'AVAILABLE');
      
      const unitOptions = currentUnit ? [currentUnit, ...otherAvailableUnits] : otherAvailableUnits;
      setAvailableUnits(unitOptions);
      
      setTenantFormData({
        name: tenant.name,
        phoneNumber: tenant.phoneNumber || '',
        email: tenant.email || '',
        leaseStartDate: tenant.leaseStartDate,
        leaseEndDate: tenant.leaseEndDate,
        unitId: currentUnit ? currentUnit.id : ''
      });

      setIsModalOpen(true);
    } catch (err) { setError('ไม่สามารถโหลดข้อมูลห้องได้'); }
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingTenant(null);
  };

  const handleSaveTenant = async () => {
    try {
      if (editingTenant) {
        await api.put(`/tenants/${editingTenant.id}`, tenantFormData);
      } else {
        await api.post('/tenants', tenantFormData);
      }
      handleCloseModal();
      fetchTenants();
      // TODO: Add a success toast/notification
    } catch (err) {
      setError(err.response?.data?.message || 'เกิดข้อผิดพลาดในการบันทึกข้อมูล');
      console.error(err);
    }
  };
  
  const handleDeleteTenant = async (tenantId) => {
    if (window.confirm('คุณแน่ใจหรือไม่ว่าต้องการลบข้อมูลผู้เช่าคนนี้? การกระทำนี้จะทำให้ห้องกลับมาว่าง')) {
      try {
        await api.delete(`/tenants/${tenantId}`);
        fetchTenants();
        // TODO: Add a success toast/notification
      } catch (err) {
        setError('เกิดข้อผิดพลาดในการลบข้อมูล');
        console.error(err);
      }
    }
  };

  const formatDate = (dateString) => new Date(dateString).toLocaleDateString('th-TH', { year: 'numeric', month: 'long', day: 'numeric' });

  return (
    <div>
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <h2 className="text-3xl font-bold text-slate-800">จัดการข้อมูลผู้เช่า</h2>
        <div className="w-full sm:w-auto flex sm:justify-end gap-2">
            <input 
                type="text"
                placeholder="ค้นหาชื่อ หรือ ห้อง..."
                className="w-full sm:w-64 p-2 border border-gray-300 rounded-lg shadow-sm"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button onClick={handleOpenCreateModal} className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 flex items-center shadow-sm flex-shrink-0">
                <AddIcon />
                <span className="hidden sm:inline">เพิ่มผู้เช่า</span>
            </button>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md overflow-x-auto">
        {loading ? <p className="p-10 text-center text-gray-500">Loading...</p> : error ? <p className="p-10 text-center text-red-500">{error}</p> : (
          <table className="w-full text-left">
            <thead className="bg-slate-50 border-b">
              <tr>
                <th className="py-3 px-4 font-semibold text-slate-600">ห้อง</th>
                <th className="py-3 px-4 font-semibold text-slate-600">ชื่อผู้เช่า</th>
                <th className="py-3 px-4 font-semibold text-slate-600">เบอร์โทรศัพท์</th>
                <th className="py-3 px-4 font-semibold text-slate-600">วันเริ่มสัญญา</th>
                <th className="py-3 px-4 font-semibold text-slate-600">วันสิ้นสุดสัญญา</th>
                <th className="py-3 px-4 font-semibold text-slate-600">จัดการ</th>
              </tr>
            </thead>
            <tbody>
              {filteredTenants.length > 0 ? filteredTenants.map(tenant => (
                <tr key={tenant.id} className="border-b hover:bg-slate-50">
                  <td className="py-3 px-4">{tenant.roomNumber}</td>
                  <td className="py-3 px-4 font-medium text-slate-800">{tenant.name}</td>
                  <td className="py-3 px-4 text-slate-600">{tenant.phoneNumber}</td>
                  <td className="py-3 px-4 text-slate-600">{formatDate(tenant.leaseStartDate)}</td>
                  <td className="py-3 px-4 text-slate-600">{formatDate(tenant.leaseEndDate)}</td>
                  <td className="py-3 px-4">
                    <div className="flex items-center gap-4">
                      <button onClick={() => handleOpenEditModal(tenant)} className="text-indigo-600 hover:text-indigo-800"><EditIcon /></button>
                      <button onClick={() => handleDeleteTenant(tenant.id)} className="text-red-600 hover:text-red-800"><DeleteIcon /></button>
                    </div>
                  </td>
                </tr>
              )) : (
                <tr><td colSpan="6" className="text-center py-10 text-gray-500">ไม่พบข้อมูลผู้เช่าที่ตรงกับคำค้นหา</td></tr>
              )}
            </tbody>
          </table>
        )}
      </div>

      <TenantModal 
        isOpen={isModalOpen} 
        onClose={handleCloseModal} 
        onSave={handleSaveTenant}
        availableUnits={availableUnits}
        tenantData={tenantFormData}
        setTenantData={setTenantFormData}
        isEditing={!!editingTenant}
      />
    </div>
  );
}

export default TenantPage;