import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/*
 * Componente helper para envolver cada sección de resumen de Consulta (azul/primario).
 */
@Composable
fun ResumenConsultaSection(title: String, content: @Composable () -> Unit) {
	Column(
		modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.primary,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
		) {
			Column(Modifier.padding(16.dp)) {
				content()
			}
		}
	}
}

/*
 * Componente helper para envolver cada sección de resumen de Farmacia (verde/terciario).
 */
@Composable
fun ResumenFarmaciaSection(title: String, content: @Composable () -> Unit) {
	Column(
		modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
//			color = MaterialTheme.colorScheme.tertiary,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
		) {
			Column(Modifier.padding(16.dp)) {
				content()
			}
		}
	}
}

/*
 * Componente helper genérico para mostrar una fila de detalle de resumen (usado en ambos flujos).
 */
@Composable
fun ResumenRow(icon: ImageVector, label: String, value: String) {
	Row(
		modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			icon,
			contentDescription = label,
			modifier = Modifier.size(20.dp).padding(end = 8.dp),
			tint = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = label,
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.SemiBold,
			modifier = Modifier.width(150.dp)
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyMedium,
			fontWeight = FontWeight.Normal
		)
	}
}