const API = "http://localhost:8080/api/atendimentos";

function carregarDashboard() {
    fetch(API)
        .then(res => res.json())
        .then(data => {
            const tabela = document.getElementById("tabela");
            tabela.innerHTML = "";

            data.forEach(a => {
                tabela.innerHTML += `
                    <tr>
                        <td>${a.id}</td>
                        <td>${a.cliente}</td>
                        <td>${a.assunto}</td>
                        <td>${a.status}</td>
                        <td>${a.atendenteId ?? "-"}</td>
                        <td>
                            ${a.status === "EM_ATENDIMENTO"
                                ? `<button onclick="finalizar(${a.id})">Finalizar</button>`
                                : ""}
                        </td>
                    </tr>
                `;
            });
        });
}

function criarAtendimento() {
    const cliente = document.getElementById("cliente").value;
    const assunto = document.getElementById("assunto").value;

    fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cliente, assunto })
    })
    .then(() => {
        carregarDashboard();
        document.getElementById("cliente").value = "";
    });
}

function finalizar(id) {
    fetch(`${API}/${id}/finalizar`, { method: "POST" })
        .then(() => carregarDashboard());
}

setInterval(carregarDashboard, 2000);
carregarDashboard();
