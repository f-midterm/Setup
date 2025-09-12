import React, { useState, useEffect } from 'react';
import api from '../services/api';

// --- Reusable Icon Components ---
// Why: สร้าง SVG Icons เป็น component ของตัวเองเพื่อให้โค้ดในส่วน JSX สะอาดขึ้น
const VacantIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 9l4-4 4 4m0 6l-4 4-4-4" /></svg>;
const OccupiedIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636" /></svg>;
const MaintenanceIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" /></svg>;
const LeaseIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>;


// --- Reusable StatCard Component ---
// Why: สร้าง Component สำหรับการ์ดสรุปข้อมูล 4 อันด้านบน ทำให้โค้ดไม่ซ้ำซ้อน
const StatCard = ({ title, count, icon, color }) => {
  const bgColor = `bg-${color}-100`;
  return (
    <div className="bg-white p-6 rounded-lg shadow-md flex items-center">
      <div className={`p-3 rounded-full ${bgColor}`}>
        {icon}
      </div>
      <div className="ml-4">
        <p className="text-gray-500">{title}</p>
        <p className="text-2xl font-bold text-slate-800">{count}</p>
      </div>
    </div>
  );
};

// --- Reusable RoomCard Component ---
const RoomCard = ({ unit }) => {
  const isOccupied = unit.status === 'OCCUPIED';
  const bgColor = isOccupied ? 'bg-red-100' : 'bg-green-100';
  const textColor = isOccupied ? 'text-red-700' : 'text-green-700';

  return (
    <div className={`p-4 rounded-lg shadow-sm text-center cursor-pointer transition-transform hover:scale-105 ${bgColor}`}>
      <p className="font-bold text-lg text-slate-800">{unit.roomNumber}</p>
      <p className={`text-sm font-semibold ${textColor}`}>{isOccupied ? 'ไม่ว่าง' : 'ว่าง'}</p>
    </div>
  );
};

function DashboardPage() {
  const [units, setUnits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  
  // MOCK DATA: เราจะใช้ข้อมูลจำลองนี้ไปก่อนจนกว่าจะมี API จริง
  const [summaryData, setSummaryData] = useState({
    vacant: 0,
    occupied: 0,
    maintenance: 2, // Mock data
    expiringLeases: 1, // Mock data
  });

  useEffect(() => {
    const fetchUnits = async () => {
      try {
        setLoading(true);
        const response = await api.get('/units');
        const sortedUnits = response.data.sort((a, b) => a.roomNumber.localeCompare(b.roomNumber));
        setUnits(sortedUnits);

        // Update summary data with real data from units API
        const vacantCount = sortedUnits.filter(u => u.status === 'AVAILABLE').length;
        const occupiedCount = sortedUnits.filter(u => u.status === 'OCCUPIED').length;
        
        // Why: เราจะอัปเดตเฉพาะส่วนที่เรามีข้อมูลจริงก่อน
        // ส่วน maintenance และ expiringLeases ยังคงใช้ข้อมูลจำลอง
        setSummaryData(prevData => ({
          ...prevData,
          vacant: vacantCount,
          occupied: occupiedCount,
        }));

      } catch (err) {
        setError('Failed to load room data. Please try again.');
        console.error("Failed to fetch units:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchUnits();
  }, []);

  const renderContent = () => {
    if (loading) return <div className="text-center p-10">Loading...</div>;
    if (error) return <div className="text-center text-red-500 p-10">{error}</div>;

    const floor1Units = units.filter(u => u.floor === 1);
    const floor2Units = units.filter(u => u.floor === 2);

    return (
      <div>
        <h3 className="text-xl font-semibold mb-4 text-slate-700">สถานะห้องพัก</h3>
        <div className="mb-6">
          <h4 className="font-bold text-slate-600 mb-3">ชั้น 1</h4>
          <div className="grid grid-cols-2 sm:grid-cols-4 md:grid-cols-6 lg:grid-cols-12 gap-4">
            {floor1Units.map(unit => <RoomCard key={unit.id} unit={unit} />)}
          </div>
        </div>
        <div>
          <h4 className="font-bold text-slate-600 mb-3">ชั้น 2</h4>
          <div className="grid grid-cols-2 sm:grid-cols-4 md:grid-cols-6 lg:grid-cols-12 gap-4">
            {floor2Units.map(unit => <RoomCard key={unit.id} unit={unit} />)}
          </div>
        </div>
      </div>
    );
  };

  return (
    <div>
      <h2 className="text-3xl font-bold mb-6 text-slate-800">แดชบอร์ดภาพรวม</h2>
      
      {/* Summary Cards Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard title="ห้องว่าง" count={summaryData.vacant} icon={<VacantIcon />} color="green" />
        <StatCard title="ห้องไม่ว่าง" count={summaryData.occupied} icon={<OccupiedIcon />} color="red" />
        <StatCard title="แจ้งซ่อม" count={summaryData.maintenance} icon={<MaintenanceIcon />} color="yellow" />
        <StatCard title="สัญญาใกล้หมดอายุ" count={summaryData.expiringLeases} icon={<LeaseIcon />} color="blue" />
      </div>

      {/* Room Status Section */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        {renderContent()}
      </div>
    </div>
  );
}

export default DashboardPage;