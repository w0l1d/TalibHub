import ColumnSettings = DataTables.ColumnSettings;


const columns: ColumnSettings[] = [{
  title: "CNE",
  data: "cne"
}, {
  title: "Full Name",
  data: "firstName",
  render: (data: any, type: any, full: any) => data + " " + full.lastName
}, {
  data: "lastName",
  visible: false
}, {
  title: "Birth date",
  data: "birthDate",
  render: (data: any, type: any, full: any) => new Date(data).toLocaleDateString()
}, {
  title: "Email",
  data: "email"
}, {
  title: "Phone",
  data: "phone"
}, {
  title: "Promotion",
  data: "enrollmentYear",
  render: (data: any, type: any, full: any) => data + " - " + (+data + 1)
}, {
  title: "Enabled",
  data: "enabled",
  render: (data: any, type: any, full: any) =>
    `<span class="inline-block text-white px-2 py-1 text-xs rounded ${data ? "bg-green-500" : "bg-red-500"}">${data ? "Yes" : "No"}</span>`
}, {
  title: "Actions",
  orderable: false,
  className: "all",
  data: "id",
  render: (data: any, type: any, full: any) =>
    `<a href="/students/${data}" class="px-2 py-1 bg-blue-500 text-white text-sm">Edit</a>`
}];

export default columns;
